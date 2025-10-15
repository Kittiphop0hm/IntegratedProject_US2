import { decodeJWT } from "@/libs/decodeJWT"
import { defineStore, acceptHMRUpdate } from "pinia"
import { ref } from "vue"

export const useCountNewOrder = defineStore("countNewOrder", () => {
    const countNewOrder = ref(0)

    const getCountNewOrder = () => countNewOrder.value

    const setCountNewOrder = (count) => {
        countNewOrder.value = count
    }

    const fetchCountNewOrder = async () => {
        try {
        const accessToken = sessionStorage.getItem("accessToken")
        if (accessToken) {
            const decodeToken = decodeJWT(accessToken)
            const data = await fetch(`${import.meta.env.VITE_APP_URL}/v2/sellers/${decodeToken.id}/orders/count/newOrder?orderStatus=COMPLETED&isNewOrder=true`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                    Accept: "application/json"
                },
            })
            const items = await data.json()
            setCountNewOrder(items)
        }
        } catch(error) {
            console.error("Error fetching count of new orders:", error)
        }
    }
    
    return { countNewOrder, getCountNewOrder, setCountNewOrder, fetchCountNewOrder }
})

if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useCountNewOrder, import.meta.hot))
}


