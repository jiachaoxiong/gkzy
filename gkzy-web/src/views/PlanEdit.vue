<template>
  <Layout>
    <el-button @click="$router.back()">← 返回</el-button>
    <h3 style="margin:16px 0">{{ plan?.name }} — 志愿表 ({{ details.length }}/8)</h3>

    <el-card v-for="(d, i) in details" :key="d.id" style="margin-bottom:8px;">
      <div style="display:flex;justify-content:space-between;align-items:center;">
        <div style="display:flex;align-items:center;gap:12px;flex-wrap:wrap;">
          <strong>志愿{{ i + 1 }}</strong>
          <span style="font-size:16px;font-weight:600;">{{ d.collegeName || '未知院校' }}</span>
          <el-tag size="small" :type="d.collegeLevel === '985' ? 'danger' : d.collegeLevel === '211' ? 'warning' : d.collegeLevel === '双一流' ? 'success' : 'info'">
            {{ d.collegeLevel || '-' }}
          </el-tag>
          <span style="color:#909399;font-size:13px;">{{ d.collegeProvince || '' }} {{ d.collegeCity || '' }}</span>
          <el-tag :type="d.strategy==='冲'?'danger':d.strategy==='稳'?'warning':'success'" size="small">
            {{ d.strategy }}
          </el-tag>
          <span style="color:#409EFF;">录取概率: {{ d.admitProbability }}%</span>
        </div>
        <div>
          <el-button size="small" @click="deleteDetail(d.id)" type="danger">删除</el-button>
        </div>
      </div>
      <!-- 已选专业 -->
      <div v-if="getMajorNames(d).length" style="margin-top:8px;display:flex;align-items:center;gap:6px;flex-wrap:wrap;">
        <span style="color:#909399;font-size:12px;">📚 专业:</span>
        <el-tag v-for="(name, idx) in getMajorNames(d)" :key="idx" size="small" type="info" effect="plain">{{ idx + 1 }}. {{ name }}</el-tag>
      </div>
    </el-card>

    <el-empty v-if="!details.length" description="暂未添加志愿" />

    <el-button type="primary" @click="submitPlan" :disabled="!details.length">模拟提交</el-button>
  </Layout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Layout from './Layout.vue'
import { planApi } from '@/api/plan'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const plan = ref<any>(null)
const details = ref<any[]>([])

onMounted(async () => {
  const id = Number(route.params.id)
  plan.value = await planApi.detail(id)
  details.value = await planApi.getDetails(id) as any[]
})

async function deleteDetail(did: number) {
  try {
    await ElMessageBox.confirm('确定删除该志愿吗？', '提示', { type: 'warning' })
  } catch {
    return // 用户取消
  }
  try {
    await planApi.deleteDetail(plan.value.id, did)
    ElMessage.success('已删除')
    details.value = (await planApi.getDetails(plan.value.id) || []) as any[]
  } catch {
    ElMessage.error('删除失败')
  }
}

function getMajorNames(d: any): string[] {
  return [d.major1Name, d.major2Name, d.major3Name, d.major4Name, d.major5Name, d.major6Name].filter(Boolean)
}

async function submitPlan() {
  await planApi.submit(plan.value.id)
  ElMessage.success('志愿表已提交！')
  router.push('/plans')
}
</script>
