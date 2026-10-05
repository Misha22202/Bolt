import { reactive, readonly } from "vue"

const state = reactive({
    isOpen: false,
})

export function useContactModal() {
    function open() {
        state.isOpen = true
    }

    function close() {
        state.isOpen = false
    }

    return {
        state: readonly(state),
        open,
        close
    }
}