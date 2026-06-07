<template>
  <Layout>
    <el-card>
      <template #header>
        <el-row :gutter="12">
          <el-col :span="5">
            <el-select v-model="provinceFilter" placeholder="选择省份" clearable filterable @change="onProvinceChange">
              <el-option v-for="p in provinces" :key="p" :label="p" :value="p" />
            </el-select>
          </el-col>
          <el-col :span="5"><el-input v-model="keyword" placeholder="搜索院校名称" clearable @clear="fetchData" @keyup.enter="fetchData" /></el-col>
          <el-col :span="4">
            <el-select v-model="levelFilter" placeholder="办学层次" clearable @change="fetchData">
              <el-option label="985" value="985" /><el-option label="211" value="211" />
              <el-option label="双一流" value="双一流" /><el-option label="普通" value="普通" />
            </el-select>
          </el-col>
          <el-col :span="3">
            <el-button type="primary" @click="fetchData">搜索</el-button>
          </el-col>
        </el-row>
      </template>
      <el-table :data="colleges" stripe v-loading="loading">
        <el-table-column prop="name" label="院校名称" min-width="160" />
        <el-table-column prop="code" label="院校代码" width="90" />
        <el-table-column prop="province" label="省份" width="80" />
        <el-table-column prop="city" label="城市" width="90" />
        <el-table-column prop="level" label="层次" width="80">
          <template #default="{ row }"><el-tag size="small" :type="row.level === '985' ? 'danger' : row.level === '211' ? 'warning' : row.level === '双一流' ? 'success' : 'info'">{{ row.level }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="80">
          <template #default="{ row }"><el-tag size="small">{{ row.type }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="80" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click="$router.push(`/colleges/${row.id}`)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:16px;justify-content:center" background layout="prev,pager,next"
        :total="total" :page-size="size" v-model:current-page="page" @current-change="fetchData" />
    </el-card>
  </Layout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import Layout from './Layout.vue'
import { collegeApi } from '@/api/college'

const colleges = ref([])
const provinces = ref<string[]>([])
const loading = ref(false)
const keyword = ref('')
const levelFilter = ref('')
const provinceFilter = ref('')
const page = ref(1)
const size = ref(20)
const total = ref(0)

onMounted(async () => {
  provinces.value = await collegeApi.provinces() as string[] || []
  fetchData()
})

function onProvinceChange() {
  page.value = 1
  fetchData()
}

async function fetchData() {
  loading.value = true
  try {
    const res: any = await collegeApi.list({ page: page.value, size: size.value, keyword: keyword.value, level: levelFilter.value, province: provinceFilter.value })
    colleges.value = res.records
    total.value = res.total
  } finally { loading.value = false }
}
</script>
