import request from '@/utils/request'

export const planApi = {
  list() {
    return request.get('/plans')
  },
  create(data: any) {
    return request.post('/plans', data)
  },
  detail(id: number) {
    return request.get(`/plans/${id}`)
  },
  update(id: number, data: any) {
    return request.put(`/plans/${id}`, data)
  },
  delete(id: number) {
    return request.delete(`/plans/${id}`)
  },
  getDetails(id: number) {
    return request.get(`/plans/${id}/details`)
  },
  addDetail(id: number, data: any) {
    return request.post(`/plans/${id}/details`, data)
  },
  updateDetail(planId: number, detailId: number, data: any) {
    return request.put(`/plans/${planId}/details/${detailId}`, data)
  },
  deleteDetail(planId: number, detailId: number) {
    return request.delete(`/plans/${planId}/details/${detailId}`)
  },
  submit(id: number) {
    return request.post(`/plans/${id}/submit`)
  }
}
