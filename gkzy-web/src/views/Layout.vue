<template>
  <el-container class="layout">
    <!-- 手机端顶部导航栏 -->
    <div class="mobile-header" v-if="isMobile">
      <el-button type="primary" link @click="drawerOpen = true">
        <el-icon :size="22"><Menu /></el-icon>
      </el-button>
      <span class="mobile-logo">🎓 高考志愿填报系统</span>
      <span style="color:#909399;font-size:13px;">{{ userStore.user?.nickname || userStore.user?.username }}</span>
    </div>

    <!-- 手机端：抽屉菜单 -->
    <el-drawer v-model="drawerOpen" direction="ltr" size="220px" :with-header="false" v-if="isMobile">
      <div class="drawer-menu">
        <div class="logo">🎓 高考志愿填报系统</div>
        <el-menu :default-active="route.path" router background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF" @select="drawerOpen = false">
          <el-menu-item index="/dashboard"><el-icon><HomeFilled /></el-icon> 工作台</el-menu-item>
          <el-menu-item index="/colleges"><el-icon><Search /></el-icon> 院校查询</el-menu-item>
          <el-menu-item index="/recommend"><el-icon><TrendCharts /></el-icon> 智能推荐</el-menu-item>
          <el-menu-item index="/plans"><el-icon><Document /></el-icon> 我的方案</el-menu-item>
        </el-menu>
        <div class="user-info">
          {{ userStore.user?.nickname || userStore.user?.username }}
          <el-button type="danger" size="small" @click="logout">退出</el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 桌面端：固定侧边栏 -->
    <el-aside :width="isMobile ? '0px' : '220px'" class="aside" v-if="!isMobile">
      <div class="logo">🎓 高考志愿填报系统</div>
      <el-menu :default-active="route.path" router background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF">
        <el-menu-item index="/dashboard"><el-icon><HomeFilled /></el-icon> 工作台</el-menu-item>
        <el-menu-item index="/colleges"><el-icon><Search /></el-icon> 院校查询</el-menu-item>
        <el-menu-item index="/recommend"><el-icon><TrendCharts /></el-icon> 智能推荐</el-menu-item>
        <el-menu-item index="/plans"><el-icon><Document /></el-icon> 我的方案</el-menu-item>
      </el-menu>
      <div class="user-info">
        {{ userStore.user?.nickname || userStore.user?.username }}
        <el-button type="danger" size="small" @click="logout">退出</el-button>
      </div>
    </el-aside>

    <el-main :style="isMobile ? 'padding:12px;margin-top:50px' : ''"><slot /></el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const drawerOpen = ref(false)

// 响应式检测
const isMobile = ref(false)
function checkMobile() {
  isMobile.value = window.innerWidth < 768
}
onMounted(() => { checkMobile(); window.addEventListener('resize', checkMobile) })
onUnmounted(() => window.removeEventListener('resize', checkMobile))

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

/* 手机端顶部栏 */
.mobile-header {
  position: fixed; top: 0; left: 0; right: 0; z-index: 1000;
  display: flex; align-items: center; justify-content: space-between;
  height: 48px; padding: 0 12px;
  background: #304156; color: #fff;
}
.mobile-logo { font-size: 14px; font-weight: bold; }

/* 抽屉菜单 */
.drawer-menu {
  display: flex; flex-direction: column; height: 100%;
  background: #304156;
}
.drawer-menu .logo {
  color: #fff; text-align: center; padding: 16px 4px;
  font-size: 15px; font-weight: bold; border-bottom: 1px solid #4a5d70;
}
.drawer-menu .user-info {
  color: #bfcbd9; padding: 16px; text-align: center;
  margin-top: auto; border-top: 1px solid #4a5d70;
}
</style>
