<script setup>
import Navbar from '@/views/Navbar.vue';
import Search from '../Search.vue';
import { onMounted, ref, watchEffect } from 'vue';
import { useRoute } from 'vue-router';
 
const emits = defineEmits([
    'fecthItemFromPage', 'toPageFirst', 'toPageLast', 'toNextPage', 'toPrevPage', 'changePageSize', 'reportOrderStatus'
])
 
const props = defineProps({
    orders: {
        type: Object,
        required: true
    },
    page: {
        type: Number
    },
    pageSize: {
        type: Number,
    },
    pageNumber: {
        type: Array,
        required: true
    },
    userRole: {
        type: String,
        required: true
    },
    orderStatus: {
        type: String,
        required: true
    },

})

const route = useRoute()
const myOrders = ref([])
const myOrderCompleted = ref([])
const myOrderCancel = ref([])
const pageNumberArr = ref([])
const size = ref()
console.log(route.path);

onMounted(() => {
    console.log(props.orderStatus);
    
})

watchEffect(() => {
    props.orders ? myOrders.value = props.orders : []
    props.orders.content ? myOrderCompleted.value = props.orders.content.filter((order) => order.orderStatus === "COMPLETED") : []
    props.orders.content ? myOrderCancel.value = props.orders.content.filter((order) => order.orderStatus === "CANCELED") : []
    props.pageNumber ? pageNumberArr.value = props.pageNumber : []
    props.pageSize >= 5 ? size.value = props.pageSize : 10
})
const dateFormat = (isoDate) => {
    const date = new Date(isoDate)
    const optionFormat = { year: "numeric", month: "long", day: "numeric" }
    const formatted = date.toLocaleDateString('en-US', optionFormat)
    return formatted
}
 
const totalPrice = (orderItems) => {
    const total = orderItems?.reduce((acc, current) => {
        acc += current.price * current.quantity
        return acc
    }, 0)
    return total
}
 
</script>
 
<template>
    <div>
        <Navbar />
        <Search />
        <div class="w-full h-full p-5 space-y-3">
            <div class="w-full h-full bg-gray-200 rounded-2xl p-5">
                <div class="w-full h-[50px] flex items-center">
                    <div class="w-full flex justify-between items-center space-x-4 mx-5">
                        <div class="w-full h-full space-x-4">
                            <button
                                v-if="route.path === '/sale-orders'"
                                @click="$emit('reportOrderStatus', 'new')"
                                :class="orderStatus === 'new' ? 'relative border-b font-semibold pt-3' : 'relative cursor-pointer hover:border-b pt-3'"
                                > 
                                
                                    New
                            </button>
                            <button
                                @click="$emit('reportOrderStatus', 'completed')"
                                :class="orderStatus === 'completed' ? 'border-b font-semibold' : 'cursor-pointer hover:border-b'">All</button>
                            <button
                                @click="$emit('reportOrderStatus', 'canceled')"
                                :class="orderStatus === 'canceled' ? 'border-b font-semibold' : 'cursor-pointer hover:border-b'">Canceled</button>
                        </div>
                        <div>
                            <select v-model="size" @change="$emit('changePageSize', $event)" class="border p-2">
                                <option :value="5">5</option>
                                <option :value="10">10</option>
                                <option :value="20">20</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="itbms-row w-full h-full bg-gray-200 rounded-2xl p-5">
                <div>
                    <div v-for="order in orders.content" :key="order.id"
                        class="bg-gray-100 mb-3 rounded-2xl p-5">
                        <router-link :to="{ name: 'SellerOrderDetail', params: { orderId: order.id } }">
                            <div class="flex justify-center items-center text-center space-x-10 mb-5">
                                <!-- เพิ่ม: แสดง username ตาม role -->
                                <div v-if="userRole === 'BUYER'">
                                    <h1 class="font-semibold">Seller</h1>
                                    <p class="itbms-nickname">{{ order.seller?.username }}</p>
                                </div>
                                <div v-else-if="userRole === 'SELLER'">
                                    <h1 class="font-semibold">Buyer</h1>
                                    <p class="itbms-nickname">{{ order.buyer?.username }}</p>
                                </div>
                                <div>
                                    <h1 class="font-semibold">OrderNo</h1>
                                    <p class="itbms-order-id">{{ order.id }}</p>
                                </div>
                                <div>
                                    <h1 class="font-semibold">Order Date</h1>
                                    <p class="itbms-order-date">{{ dateFormat(order.orderDate) }}</p>
                                </div>
                                <div>
                                    <h1 class="font-semibold">Payment Date</h1>
                                    <p class="itbms-payment-date">{{ dateFormat(order.paymentDate) }}</p>
                                </div>
                                <div>
                                    <h1 class="font-semibold">Total</h1>
                                    <p class="itbms-total-order-price">{{ totalPrice(order.orderItems).toLocaleString()
                                    }}</p>
                                </div>
                                <div>
                                    <h1 class="font-semibold">Status</h1>
                                    <p class="itbms-order-status">{{ order.orderStatus }}</p>
                                </div>
                                <div>
                                    <h1 class="font-semibold">Shipped to</h1>
                                    <p class="itbms-shipping-address">{{ order.shippingAddress }}</p>
                                </div>
                                <div>
                                    <h1 class="font-semibold">Note</h1>
                                    <p class="itbms-order-note">{{ order.orderNote }}</p>
                                </div>
                            </div>
                            <div v-for="orderItem in order.orderItems">
                                <div class="w-full h-full bg-white rounded-lg">
                                    <div class="itbms-item-row w-full h-full flex justify-around items-center p-3 mb-3">
                                        <div>
                                            <img src="/images/Iphone 14-pro-1.png" alt="SaleItem-image" width="100">
                                        </div>
                                        <div>
                                            <p class="itbms-item-description">{{ orderItem.description }}</p>
                                        </div>
                                        <div>
                                            <p>
                                                <span class="font-semibold">Qty:</span>
                                                <span class="itbms-item-quantity">{{ orderItem.quantity }}</span>
                                            </p>
                                        </div>
                                        <div>
                                            <p>
                                                <span class="font-semibold">Price:</span>
                                                <span class="itbms-item-total-price">{{ orderItem.price *
                                                    orderItem.quantity }}</span>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </router-link>
                    </div>
                </div>
                <div class="w-full h-full flex justify-center items-center p-5">
                    <div class="flex justify-center items-center space-x-2">
                        <div class="space-x-2">
                            <button :disabled="!orders.content?.length" @click="$emit('toPageFirst')" :class="!orders.content?.length ? 'border py-2 px-4 rounded-2xl cursor-not-allowed' : 'border py-2 px-4 rounded-2xl cursor-pointer'">First</button>
                            <button :disabled="!orders.content?.length" @click="$emit('toPrevPage')" :class="!orders.content?.length ? 'border py-2 px-4 rounded-2xl cursor-not-allowed' : 'border py-2 px-4 rounded-2xl cursor-pointer'">Perv</button>
                        </div>
                        <div v-for="index in pageNumberArr" :key="index">
                            <button @click="$emit('fecthItemFromPage', index)" :class="index - 1 === page ? 'bg-green-600 text-white border py-2 px-4 rounded-2xl cursor-pointer' : 'border py-2 px-4 rounded-2xl cursor-pointer'">
                                {{ index }}
                            </button>
                        </div>
                        <div>
                            <button :disabled="!orders.content?.length" @click="$emit('toNextPage')" :class="!orders.content?.length ? 'border py-2 px-4 rounded-2xl cursor-not-allowed' : 'border py-2 px-4 rounded-2xl cursor-pointer'">Next</button>
                        </div>
                        <div>
                            <button :disabled="!orders.content?.length" @click="$emit('toPageLast')" :class="!orders.content?.length ? 'border py-2 px-4 rounded-2xl cursor-not-allowed' : 'border py-2 px-4 rounded-2xl cursor-pointer'">Last</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>