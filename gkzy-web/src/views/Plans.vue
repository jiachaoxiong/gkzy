<template>
  <Layout>
    <el-button type="primary" @click="showCreate = true">+ 新建方案</el-button>
    <el-table :data="plans" stripe style="margin-top:16px" v-loading="loading">
      <el-table-column prop="name" label="方案名称" />
      <el-table-column prop="batch" label="批次" width="120" />
      <el-table-column prop="totalScore" label="分数" width="80" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status==='已提交'?'success':''">{{ row.status }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="240">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/plans/${row.id}/edit`)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showCreate" title="新建志愿方案">
      <el-form :model="newPlan" label-width="80px">
        <el-form-item label="方案名称"><el-input v-model="newPlan.name" /></el-form-item>
        <el-form-item label="批次">
          <el-select v-model="newPlan.batch">
            <el-option v-for="b in batches" :key="b" :label="b" :value="b" />
          </el-select>
        </el-form-item>
        <el-form-item label="科类">
          <el-select v-model="newPlan.subjectType">
            <el-option label="文史类" value="文史" /><el-option label="理工类" value="理工" />
          </el-select>
        </el-form-item>
        <el-form-item label="分数"><el-input-number v-model="newPlan.totalScore" :min="0" :max="750" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreate = false">取消</el-button>
        <el-button type="primary" @click="createPlan">创建</el-button>
      </template>
    </el-dialog>
  </Layout>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import Layout from './Layout.vue'
import { planApi } from '@/api/plan'
import { ElMessage, ElMessageBox } from 'element-plus'

const plans = ref([])
const loading = ref(false)
const showCreate = ref(false)
const batches = ['本科一批A', '本科一批B', '本科二批A', '本科二批B']
const newPlan = reactive({ name: '', batch: '本科一批A', subjectType: '文史', totalScore: 0 })

async function fetchPlans() {
  loading.value = true
  try { plans.value = await planApi.list() as any }
  finally { loading.value = false }
}

async function createPlan() {
  await planApi.create({ ...newPlan, year: 2026 })
  ElMessage.success('创建成功')
  showCreate.value = false
  fetchPlans()
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm('确定删除?', '提示', { type: 'warning' })
  await planApi.delete(id)
  ElMessage.success('已删除')
  fetchPlans()
}

fetchPlans()
</script>
