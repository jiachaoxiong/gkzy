<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2>🎓 高考志愿填报模拟系统</h2>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm" :rules="loginRules" ref="loginRef">
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="User" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="Lock" show-password />
            </el-form-item>
            <el-button type="primary" :loading="loading" @click="handleLogin" style="width:100%">登录</el-button>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <el-form :model="regForm" :rules="regRules" ref="regRef">
            <el-form-item prop="username">
              <el-input v-model="regForm.username" placeholder="用户名" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="regForm.password" type="password" placeholder="密码(6-32位)" show-password />
            </el-form-item>
            <el-form-item prop="phone">
              <el-input v-model="regForm.phone" placeholder="手机号" />
            </el-form-item>
            <el-form-item prop="subjectType">
              <el-select v-model="regForm.subjectType" placeholder="科类" style="width:100%">
                <el-option label="文史类" value="文史" />
                <el-option label="理工类" value="理工" />
              </el-select>
            </el-form-item>
            <el-button type="primary" :loading="loading" @click="handleRegister" style="width:100%">注册</el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { authApi } from '@/api/auth'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const activeTab = ref('login')
const loading = ref(false)
const loginRef = ref<FormInstance>()
const regRef = ref<FormInstance>()

const loginForm = reactive({ username: '', password: '' })
const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const regForm = reactive({ username: '', password: '', phone: '', subjectType: '' })
const regRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, min: 6, message: '密码至少6位', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
}

async function handleLogin() {
  if (!loginRef.value) return
  const valid = await loginRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res: any = await authApi.login(loginForm)
    userStore.setToken(res.token)
    await userStore.fetchUser()
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch { /* 拦截器已处理 */ }
  finally { loading.value = false }
}

async function handleRegister() {
  if (!regRef.value) return
  const valid = await regRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res: any = await authApi.register(regForm)
    userStore.setToken(res.token)
    await userStore.fetchUser()
    ElMessage.success('注册成功')
    router.push('/dashboard')
  } catch { /* 拦截器已处理 */ }
  finally { loading.value = false }
}
</script>

<style scoped>
.login-page { display: flex; align-items: center; justify-content: center; height: 100vh; background: #f0f2f5; }
.login-card { width: 420px; }
.login-card h2 { text-align: center; margin-bottom: 20px; }
</style>
