<script setup>
import { onMounted, ref } from 'vue';
import OrderHistory from './OrderHistory.vue';
import { getItemsWithToken } from '@/libs/fetchUtil';
import { decodeJWT } from '@/libs/decodeJWT';
import { useRouter } from 'vue-router';

const router = useRouter()
const orders = ref([])
const page = 0
const size = 0
onMounted(async () => {
    const accessToken = sessionStorage.getItem("accessToken")
    if (!accessToken) router.push({name: "Login"})
    const user = decodeJWT(accessToken)
    if (user.role === 'SELLER') {
        orders.value = await getItemsWithToken(`${import.meta.env.VITE_APP_URL}/v2/sellers/${user.id}/orders?page=${page}`, accessToken)   
        console.log(orders.value.content);      
    }
})
</script>

<template>
    <div>
        <OrderHistory :orders="orders" />
    </div>
</template>