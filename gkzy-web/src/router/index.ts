import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue'),
      meta: { guest: true }
    },
    {
      path: '/',
      redirect: '/dashboard'
    },
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: () => import('@/views/Dashboard.vue'),
      meta: { auth: true }
    },
    {
      path: '/colleges',
      name: 'Colleges',
      component: () => import('@/views/Colleges.vue'),
      meta: { auth: true }
    },
    {
      path: '/colleges/:id',
      name: 'CollegeDetail',
      component: () => import('@/views/CollegeDetail.vue'),
      meta: { auth: true }
    },
    {
      path: '/recommend',
      name: 'Recommend',
      component: () => import('@/views/Recommend.vue'),
      meta: { auth: true }
    },
    {
      path: '/plans',
      name: 'Plans',
      component: () => import('@/views/Plans.vue'),
      meta: { auth: true }
    },
    {
      path: '/plans/:id/edit',
      name: 'PlanEdit',
      component: () => import('@/views/PlanEdit.vue'),
      meta: { auth: true }
    }
  ]
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.auth && !token) {
    next('/login')
  } else if (to.meta.guest && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
