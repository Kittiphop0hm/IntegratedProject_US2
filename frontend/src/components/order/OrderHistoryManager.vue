<script setup>
import { onMounted, ref, watch, computed } from 'vue';
import OrderHistory from './OrderHistory.vue';
import { getItemsWithToken } from '@/libs/fetchUtil';
import { decodeJWT } from '@/libs/decodeJWT';
import { useRouter } from 'vue-router';
import { useCountNewOrder } from '@/stores/countNewOrder.js';
import { storeToRefs } from 'pinia';
 
const router = useRouter()
const orders = ref([])
const pageSession = sessionStorage.getItem('pageNumber')
const sizeSession = sessionStorage.getItem('pageSize')
const page = ref(0)
const size = ref()
const orderStatus = ref('')
const userRole = ref('')
const { getCountNewOrder, fetchCountNewOrder } = useCountNewOrder()

watch([page, size, orderStatus], () => {
    sessionStorage.setItem('pageNumber', page.value)
    sessionStorage.setItem('pageSize', size.value)
    sessionStorage.setItem('orderStatus', orderStatus.value)
    fetchData()
})
 
const fetchData = async () => {
    const accessToken = sessionStorage.getItem("accessToken")
    if (!accessToken) {
        router.push({name: "Login"})
        return
    }
    const user = decodeJWT(accessToken)
    userRole.value = user.role
   
    if (user.role === 'SELLER') {
        if (orderStatus.value === 'new') {
          orders.value = await getItemsWithToken(`${import.meta.env.VITE_APP_URL}/v2/sellers/${user.id}/orders?page=${page.value}&size=${size.value}&orderStatus=COMPLETED`, accessToken)  
          const newOrders = orders.value.content.filter((order) => order.isNewOrder)
          orders.value.content = newOrders
        } else {
          orders.value = await getItemsWithToken(`${import.meta.env.VITE_APP_URL}/v2/sellers/${user.id}/orders?page=${page.value}&size=${size.value}&orderStatus=${orderStatus.value}`, accessToken)  
          console.log(orders.value);
        }
    }
    else if (user.role === 'BUYER') {
        orders.value = await getItemsWithToken(`${import.meta.env.VITE_APP_URL}/v2/users/${user.id}/orders?page=${page.value}&size=${size.value}`, accessToken)  
        console.log('Buyer Orders:', orders.value);
    }
}
 
onMounted(async () => {
    size.value = sizeSession ? Number(sizeSession) : 10;
    page.value = pageSession ? Number(pageSession) : 0;
    orderStatus.value = 'new'
    console.log(fetchCountNewOrder())
})
 
const computedPageNumberArr = computed(() => {
  const arr = [];
  let maxDisplay = 10;
  console.log("pageNumber.value:", page.value);
  console.log("pageObj.value.totalPages:", orders.value.totalPages);
  for (let i = 0; i < orders.value.totalPages; i++) {
    arr.push(i + 1);
  }
  if (arr.length > maxDisplay) {
    let difference = arr.length - page.value;
    if (difference > maxDisplay) {
      arr.splice(
        arr.length - (difference - maxDisplay),
        Math.abs(difference - maxDisplay)
      );
      if (page.value !== 0) {
        arr.splice(0, difference - maxDisplay);
      }
    } else {
      while (arr.length !== maxDisplay) {
        arr.shift();
      }
    }
  }
  console.log("133")
  console.log(arr)
  return arr;
});
 
const fecthItemFromPage = async(index) => {
  console.log(index);
  page.value = index - 1;
  await fetchData();
};
 
const toFirst = () => {
    console.log('To first');
    page.value = 0;
}
 
const toLast = () => {
    console.log('To last');
    page.value = orders.value.totalPages - 1;
}
 
const toPrev = () => {
    page.value -= 1
}
 
const toNext = () => {
    page.value += 1
}
 
const changePageSize = (event) => {
    size.value = Number(event.target.value)
    page.value = 0
}

const repostStatus = (status) => {
  orderStatus.value = status
}
</script>
 
<template>
    <div>
        <OrderHistory
        :orders="orders"
        :pageNumber="computedPageNumberArr"
        :page="page"
        :pageSize="size"
        :userRole="userRole"
        :orderStatus="orderStatus"
        @fecthItemFromPage="fecthItemFromPage"
        @toPageFirst="toFirst"
        @toPageLast="toLast"
        @toNextPage="toNext"
        @toPrevPage="toPrev"
        @changePageSize="changePageSize"
        @reportOrderStatus="repostStatus"
        />
    </div>
</template>