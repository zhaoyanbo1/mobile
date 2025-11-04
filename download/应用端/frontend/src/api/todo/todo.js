import service from '@/utils/request'

export default {
    generateAiSuggestions() {
        return service({
            url: '/ai/todos/generate',
            method: 'post',
        })
    },
    generateAiBonusSuggestions() {
        return service({
            url: '/ai/todos/generate-bonus',
            method: 'post',
        })
    },
}