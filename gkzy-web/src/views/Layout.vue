<template>
  <el-container class="layout">
    <el-aside width="220px" class="aside">
      <div class="logo">🎓 高考志愿填报模拟系统</div>
      <el-menu :default-active="route.path" router background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF">
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon> 工作台
        </el-menu-item>
        <el-menu-item index="/colleges">
          <el-icon><Search /></el-icon> 院校查询
        </el-menu-item>
        <el-menu-item index="/recommend">
          <el-icon><TrendCharts /></el-icon> 智能推荐
        </el-menu-item>
        <el-menu-item index="/plans">
          <el-icon><Document /></el-icon> 我的方案
        </el-menu-item>
      </el-menu>
      <div class="user-info">
        {{ userStore.user?.nickname || userStore.user?.username }}
        <el-button type="danger" size="small" @click="logout">退出</el-button>
      </div>
    </el-aside>
    <el-main><slot /></el-main>
  </el-container>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

function logout() {
  userStore.clearToken()
  router.push('/login')
}
</script>

<style scoped>
.layout { height: 100vh; }
.aside { background: #304156; display: flex; flex-direction: column; }
.logo { color: #fff; text-align: center; padding: 20px 4px; font-size: 16px; font-weight: bold; border-bottom: 1px solid #4a5d70; white-space: nowrap; }
.user-info { color: #bfcbd9; padding: 16px; text-align: center; margin-top: auto; border-top: 1px solid #4a5d70; }
</style>
