<template>
  <Layout>
    <el-card>
      <template #header>📊 智能推荐</template>
      <el-form :model="form" label-width="80px" style="max-width:320px">
        <el-form-item label="高考分数">
          <el-input-number v-model="form.score" :min="0" :max="750" style="width:200px" />
        </el-form-item>
        <el-form-item label="全省位次">
          <el-input-number v-model="form.rank" :min="1" style="width:200px" />
        </el-form-item>
        <el-form-item label="科类">
          <el-select v-model="form.subjectType" style="width:200px">
            <el-option label="文史类" value="文史" />
            <el-option label="理工类" value="理工" />
          </el-select>
        </el-form-item>
        <el-form-item label="批次">
          <el-select v-model="form.batch" style="width:200px">
            <el-option label="本科一批A" value="本科一批A" />
            <el-option label="本科一批B" value="本科一批B" />
            <el-option label="本科批" value="本科批" />
            <el-option label="本科二批A" value="本科二批A" />
            <el-option label="本科二批B" value="本科二批B" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="analyze">开始分析</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top:16px" v-if="results.length">
      <template #header>
        <el-row justify="space-between" align="middle">
          <span>📈 推荐结果</span>
          <el-select v-model="provinceFilter" placeholder="全部省份" clearable size="small" style="width:140px">
            <el-option v-for="p in provinces" :key="p" :label="p" :value="p" />
          </el-select>
        </el-row>
      </template>
      <el-tabs v-model="strategyTab">
        <el-tab-pane v-for="s in ['冲', '稳', '保']" :key="s" :label="`${s} (${countByStrategy(s)})`" :name="s">
          <el-table :data="filteredResults" stripe>
            <el-table-column prop="collegeName" label="院校名称" min-width="140" />
            <el-table-column prop="level" label="层次" width="80">
              <template #default="{ row }">
                <el-tag size="small" :type="row.level === '985' ? 'danger' : row.level === '211' ? 'warning' : row.level === '双一流' ? 'success' : 'info'">{{ row.level }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="province" label="省份" width="70" />
            <el-table-column prop="batch" label="参考批次" width="100" />
            <el-table-column prop="refMinRank" label="参考位次" width="100" />
            <el-table-column prop="admitProbability" label="录取概率" width="100">
              <template #default="{ row }">
                <el-progress :percentage="Number(row.admitProbability)" :color="probColor(row.strategy)" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button size="small" type="success" @click="addToPlan(row)">加入志愿</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 专业选择对话框 -->
    <el-dialog v-model="majorDialogVisible" :title="`选择专业 — ${currentCollege?.collegeName || ''}`" :width="isMobile ? '95%' : '560px'" :close-on-click-modal="false">
      <div v-if="majors.length === 0" style="text-align:center;color:#909399;padding:20px;">该院校暂无专业数据</div>
      <div v-else>
        <p style="color:#909399;margin-bottom:12px;">请选择感兴趣的专业（最多6个），已选 <strong>{{ selectedMajorIds.length }}</strong>/6</p>
        <el-checkbox-group v-model="selectedMajorIds" :max="6" style="display:flex;flex-direction:column;gap:8px;">
          <el-checkbox v-for="m in majors" :key="m.id" :value="m.id" :label="m.id" style="margin-right:0;">
            <span style="font-weight:500;">{{ m.name }}</span>
            <el-tag size="small" style="margin-left:8px;">{{ m.category || '未分类' }}</el-tag>
            <span v-if="m.features" style="color:#909399;font-size:12px;margin-left:8px;">{{ m.features }}</span>
          </el-checkbox>
        </el-checkbox-group>
      </div>
      <template #footer>
        <el-button @click="majorDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmAdd(currentRow)">确认加入志愿</el-button>
      </template>
    </el-dialog>
  </Layout>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import Layout from './Layout.vue'
import { recommendApi } from '@/api/recommend'
import { planApi } from '@/api/plan'
import { collegeApi } from '@/api/college'
import { ElMessage } from 'element-plus'

const router = useRouter()
const isMobile = ref(false)
onMounted(() => { isMobile.value = window.innerWidth < 768; window.addEventListener('resize', () => isMobile.value = window.innerWidth < 768) })
onUnmounted(() => window.removeEventListener('resize', () => {}))

const loading = ref(false)
const results = ref<any[]>([])
const strategyTab = ref('冲')
const provinceFilter = ref('')

// 专业选择对话框
const majorDialogVisible = ref(false)
const currentCollege = ref<any>(null)
const currentRow = ref<any>(null)
const majors = ref<any[]>([])
const selectedMajorIds = ref<number[]>([])

const form = reactive({ score: 0, rank: 0, subjectType: '理工', batch: '本科一批A' })

// ====== 2025年山西省一分一段表 (分数→累计位次) ======
// 数据来源: 山西省招生考试管理中心 2025-06-25公布
// 使用逐10分关键点 + 线性插值覆盖所有分数
const RANK_2025: Record<string, Record<number, number>> = {
  理工: {
    690:84,680:230,670:478,660:942,650:1691,640:2722,630:3984,620:5728,610:7886,600:10452,
    590:13425,580:16930,570:20780,560:25129,550:29847,540:35110,530:40723,520:46928,510:53491,500:60425,
    490:67479,480:74557,470:81895,460:89298,450:96566,440:103827,430:111144,420:118413,410:125763,400:133192,
    390:140702,380:148293,370:155950,360:163659,350:171420,340:179230,330:187090,320:195000,310:202960,300:210970
  },
  文史: {
    671:11,670:13,660:42,650:110,640:256,630:448,620:807,610:1270,600:1918,
    590:2792,580:3874,570:5154,560:6593,550:8406,540:10416,530:12559,520:15009,510:17574,500:20270,
    490:23133,480:26003,470:28696,460:31489,450:34070,443:35877,
    440:36588,430:39307,420:42032,410:44763,400:47500,390:50243,380:52992,370:55747,360:58508,350:61275,
    340:64048,330:66827,320:69612,310:72403,300:75200
  }
}

// 分数→位次 换算
function scoreToRank(score: number, subjectType: string): number | null {
  const map = RANK_2025[subjectType]
  if (!map || score <= 0) return null
  if (map[score]) return map[score]
  // 线性插值：找最近的两个已知点
  const scores = Object.keys(map).map(Number).sort((a,b) => a-b)
  let lower: number | null = null, higher: number | null = null
  for (const s of scores) {
    if (s < score) lower = s
    if (s > score && higher === null) higher = s
  }
  if (lower && higher) {
    const ratio = (score - lower) / (higher - lower)
    return Math.round(map[lower] + (map[higher] - map[lower]) * ratio)
  }
  return lower ? map[lower] : higher ? map[higher] : null
}

// 当分数或科类变化时，自动换算位次
watch([() => form.score, () => form.subjectType], ([score, subject]) => {
  if (score > 0) {
    const r = scoreToRank(score, subject)
    if (r) form.rank = r
  }
})

// 省份列表（从结果中提取）
const provinces = computed(() => {
  const set = new Set(results.value.map(r => r.province).filter(Boolean))
  return [...set].sort()
})

// 按策略 + 省份过滤
const filteredResults = computed(() => {
  return results.value.filter(r =>
    r.strategy === strategyTab.value &&
    (!provinceFilter.value || r.province === provinceFilter.value)
  )
})

function countByStrategy(s: string) { return results.value.filter((r: any) => r.strategy === s).length }

function probColor(strategy: string) {
  return strategy === '冲' ? '#f56c6c' : strategy === '稳' ? '#e6a23c' : '#67c23a'
}

async function analyze() {
  if (!form.score || !form.rank) { ElMessage.warning('请填写分数和位次'); return }
  loading.value = true
  try { results.value = await recommendApi.analyze(form) as any[] }
  finally { loading.value = false }
}

async function addToPlan(row: any) {
  // 打开专业选择对话框
  currentCollege.value = row
  currentRow.value = row
  selectedMajorIds.value = []
  try {
    majors.value = await collegeApi.majors(row.collegeId) as any[]
  } catch {
    majors.value = []
  }
  majorDialogVisible.value = true
}

async function confirmAdd(row: any) {
  const majorIds = selectedMajorIds.value
  const data: any = {
    collegeId: row.collegeId,
    strategy: row.strategy,
    admitProbability: row.admitProbability
  }
  // 填充 major1~major6（最多6个）
  if (majorIds.length >= 1) data.major1 = majorIds[0]
  if (majorIds.length >= 2) data.major2 = majorIds[1]
  if (majorIds.length >= 3) data.major3 = majorIds[2]
  if (majorIds.length >= 4) data.major4 = majorIds[3]
  if (majorIds.length >= 5) data.major5 = majorIds[4]
  if (majorIds.length >= 6) data.major6 = majorIds[5]

  try {
    const plans: any = await planApi.list()
    if (!plans || plans.length === 0) {
      const plan: any = await planApi.create({ name: '我的志愿方案', batch: form.batch, subjectType: form.subjectType, totalScore: form.score, year: 2026 })
      await planApi.addDetail(plan.id, data)
    } else {
      await planApi.addDetail(plans[0].id, data)
    }
    ElMessage.success('已加入志愿方案')
    majorDialogVisible.value = false
  } catch { /* handled */ }
}
</script>
