import request from '@/utils/request'

export const collegeApi = {
  provinces() {
    return request.get('/provinces')
  },
  list(params: { page?: number; size?: number; keyword?: string; type?: string; level?: string; province?: string }) {
    return request.get('/colleges', { params })
  },
  detail(id: number) {
    return request.get(`/colleges/${id}`)
  },
  majors(id: number) {
    return request.get(`/colleges/${id}/majors`)
  },
  scores(id: number, params?: { batch?: string; subjectType?: string }) {
    return request.get(`/colleges/${id}/scores`, { params })
  },
  searchMajors(params: { page?: number; size?: number; keyword?: string }) {
    return request.get('/majors', { params })
  }
}
