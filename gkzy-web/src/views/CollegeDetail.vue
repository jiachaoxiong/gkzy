<template>
  <Layout>
    <el-button @click="$router.back()">← 返回</el-button>
    <el-card style="margin-top:16px" v-loading="loading">
      <template #header><h3>{{ college?.name }}</h3></template>
      <el-descriptions :column="isMobile ? 1 : 2" border>
        <el-descriptions-item label="院校代码">{{ college?.code }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ college?.type }}</el-descriptions-item>
        <el-descriptions-item label="所在地">{{ college?.province }} {{ college?.city }}</el-descriptions-item>
        <el-descriptions-item label="办学层次">{{ college?.level }}</el-descriptions-item>
        <el-descriptions-item label="官网">
            <template v-if="college?.website">
              <a :href="websiteUrl" target="_blank" rel="noopener noreferrer">{{ college?.website }}</a>
            </template>
            <span v-else style="color:#909399">暂无</span>
          </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card style="margin-top:16px">
      <template #header>📚 开设专业</template>
      <el-table :data="majors" stripe>
        <el-table-column prop="name" label="专业名称" />
        <el-table-column prop="code" label="专业代码" width="120" />
        <el-table-column prop="category" label="学科门类" width="120" />
        <el-table-column prop="features" label="特色" />
      </el-table>
    </el-card>

    <el-card style="margin-top:16px">
      <template #header>📈 历年分数线</template>
      <el-table :data="scores" stripe>
        <el-table-column prop="year" label="年份" width="80" />
        <el-table-column prop="batch" label="批次" width="120" />
        <el-table-column prop="subjectType" label="科类" width="80" />
        <el-table-column prop="minScore" label="最低分" width="80" />
        <el-table-column prop="minRank" label="最低位次" width="100" />
        <el-table-column prop="avgScore" label="平均分" width="80" />
        <el-table-column prop="enrollCount" label="录取人数" width="100">
          <template #default="{ row }">
            {{ row.enrollCount != null ? row.enrollCount : '暂无数据' }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </Layout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import Layout from './Layout.vue'
import { collegeApi } from '@/api/college'

const route = useRoute()
const isMobile = ref(false)
onMounted(() => { isMobile.value = window.innerWidth < 768; window.addEventListener('resize', () => isMobile.value = window.innerWidth < 768) })
onUnmounted(() => window.removeEventListener('resize', () => {}))

const college = ref<any>(null)
const majors = ref([])
const scores = ref([])
const loading = ref(false)

// 确保官网URL有协议头，避免被当作相对路径
const websiteUrl = computed(() => {
  const url = college.value?.website
  if (!url) return '#'
  return /^https?:\/\//i.test(url) ? url : `https://${url}`
})

onMounted(async () => {
  loading.value = true
  const id = Number(route.params.id)
  try {
    college.value = await collegeApi.detail(id)
    majors.value = await collegeApi.majors(id) as any
    scores.value = await collegeApi.scores(id) as any
  } finally { loading.value = false }
})
</script>
