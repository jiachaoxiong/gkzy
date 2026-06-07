<template>
  <Layout>
    <h3>👋 欢迎, {{ userStore.user?.nickname || userStore.user?.username }}</h3>
    <el-row :gutter="16" class="dashboard-row">
      <el-col :span="8" class="dashboard-col">
        <el-card class="dashboard-card">
          <template #header>📋 个人信息</template>
          <el-form :model="profile" label-width="80px">
            <el-form-item label="科类"><el-tag>{{ profile.subjectType || '未设置' }}</el-tag></el-form-item>
            <el-form-item label="高考分数">{{ profile.score || '未设置' }}</el-form-item>
            <el-form-item label="全省位次">{{ profile.rank || '未设置' }}</el-form-item>
            <el-button type="primary" size="small" @click="showEdit = true">编辑信息</el-button>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="8" class="dashboard-col">
        <el-card class="dashboard-card">
          <template #header>🚀 快捷入口</template>
          <div class="quick-links">
            <div class="quick-item" @click="$router.push('/recommend')">📊 智能推荐</div>
            <div class="quick-item" @click="$router.push('/colleges')">🔍 院校查询</div>
            <div class="quick-item" @click="$router.push('/plans')">📋 我的方案</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" class="dashboard-col">
        <el-card class="dashboard-card">
          <template #header>📊 方案统计</template>
          <el-statistic title="志愿方案数" :value="planCount" />
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="showEdit" title="编辑个人信息">
      <el-form :model="profile" label-width="80px">
        <el-form-item label="昵称"><el-input v-model="profile.nickname" /></el-form-item>
        <el-form-item label="科类">
          <el-select v-model="profile.subjectType">
            <el-option label="文史类" value="文史" /><el-option label="理工类" value="理工" />
          </el-select>
        </el-form-item>
        <el-form-item label="高考分数"><el-input-number v-model="profile.score" :min="0" :max="750" /></el-form-item>
        <el-form-item label="全省位次"><el-input-number v-model="profile.rank" :min="1" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEdit = false">取消</el-button>
        <el-button type="primary" @click="saveProfile">保存</el-button>
      </template>
    </el-dialog>
  </Layout>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import Layout from './Layout.vue'
import { useUserStore } from '@/stores/user'
import { authApi } from '@/api/auth'
import { planApi } from '@/api/plan'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const showEdit = ref(false)
const planCount = ref(0)
const profile = reactive({ nickname: '', subjectType: '', score: 0, rank: 0 })

onMounted(async () => {
  const user = userStore.user
  if (user) Object.assign(profile, user)
  try {
    const plans: any = await planApi.list()
    planCount.value = Array.isArray(plans) ? plans.length : 0
  } catch { /* ignore */ }
})

async function saveProfile() {
  await authApi.updateMe(profile)
  await userStore.fetchUser()
  ElMessage.success('保存成功')
  showEdit.value = false
}
</script>

<style scoped>
.dashboard-row {
  margin-top: 16px;
  align-items: stretch;
}
.dashboard-col {
  display: flex;
}
.dashboard-card {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.dashboard-card :deep(.el-card__body) {
  flex: 1;
}
.quick-links {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.quick-item {
  text-align: center;
  font-size: 14px;
  font-weight: 500;
  padding: 14px 4px;
  border-radius: 6px;
  cursor: pointer;
  background: #f5f7fa;
  transition: background .2s;
}
.quick-item:hover {
  background: #ecf5ff;
}
</style>
