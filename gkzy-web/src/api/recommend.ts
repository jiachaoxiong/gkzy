import request from '@/utils/request'

export const recommendApi = {
  analyze(data: { score: number; rank: number; subjectType: string; batch: string }) {
    return request.post('/recommend/analyze', data)
  }
}
