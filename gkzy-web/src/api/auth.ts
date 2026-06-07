import request from '@/utils/request'

export const authApi = {
  login(data: { username: string; password: string }) {
    return request.post('/auth/login', data)
  },
  register(data: { username: string; password: string; phone: string; nickname?: string; subjectType?: string }) {
    return request.post('/auth/register', data)
  },
  getMe() {
    return request.get('/auth/me')
  },
  updateMe(data: any) {
    return request.put('/auth/me', data)
  }
}
