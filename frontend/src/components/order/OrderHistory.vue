<script setup>
import Navbar from '@/views/Navbar.vue';
import Search from '../Search.vue';
import { ref, watchEffect, computed } from 'vue';

const emits = defineEmits(['fecthItemFromPage', 'toPageFirst', 'toPageLast', 'toNextPage', 'toPrevPage'])

const props = defineProps({
    orders: {
        type: Object,
        required: true
    },
    pageNumber: {
        type: Array,
        required: true
    }
})
console.log(props.pageNumber);


const myOrders = ref([])
const myOrderCompleted = ref([])
const myOrderCancel = ref([])
const pageNumberArr = ref([])
watchEffect(() => {
    props.orders ? myOrders.value = props.orders : []
    props.orders.content ? myOrderCompleted.value = props.orders.content.filter((order) => order.orderStatus === "COMPLETED") : []
    props.orders.content ? myOrderCancel.value = props.orders.content.filter((order) => order.orderStatus === "CANCELED") : []
    props.pageNumber ? pageNumberArr.value = props.pageNumber : []
    console.log(pageNumberArr.value);
})
const status = ref('completed')
const changeStatus = (text) => {
    status.value = text
}
const dateFormat = (isoDate) => {
    const date = new Date(isoDate)
    const optionFormat = { year: "numeric", month: "long", day: "numeric" }
    const formatted = date.toLocaleDateString('en-US', optionFormat)
    return formatted
}

const totalPrice = (orderItems) => {
    const total = orderItems?.reduce((acc, current) => {
        acc += current.price
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
                    <div class="space-x-4">
                        <button @click="changeStatus('completed')"
                            :class="status === 'completed' ? 'border-b font-semibold' : 'cursor-pointer hover:border-b'">Completed</button>
                        <button @click="changeStatus('canceled')"
                            :class="status === 'canceled' ? 'border-b font-semibold' : 'cursor-pointer hover:border-b'">Canceled</button>
                    </div>
                </div>
            </div>
            <div class="itbms-row w-full h-full bg-gray-200 rounded-2xl p-5">
                <div>
                    <div v-if="status === 'completed'" v-for="order in orders.content" :key="order.id"
                        class="bg-gray-100 mb-3 rounded-2xl p-5">
                        <router-link :to="{ name: 'OrderDetail', params: { orderId: order.id } }">
                            <div class="flex justify-center items-center text-center space-x-10 mb-5">
                                <div>
                                    <h1 class="font-semibold">Username</h1>
                                    <p class="itbms-nickname">{{ order.buyer.username }}</p>
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
                                    <h1 class="font-semibold">Total Price</h1>
                                    <p class="itbms-total-order-price">{{ totalPrice(order.orderItems).toLocaleString()
                                    }}</p>
                                </div>
                                <div>
                                    <h1 class="font-semibold">Status</h1>
                                    <p class="itbms-order-status">{{ order.orderStatus }}</p>
                                </div>
                                <div>
                                    <h1 class="font-semibold">Address</h1>
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

                    <!-- <div v-if="status === 'canceled'" v-for="order in myOrderCancel" :key="order.id"
                        class="bg-gray-100 mb-3 rounded-2xl p-5">
                        <router-link :to="{ name: 'OrderDetail', params: { orderId: order.id } }">
                            <div class="flex justify-center items-center text-center space-x-5 mb-5">
                                <div>
                                    <h1 class="font-semibold">Username</h1>
                                    <p class="itbms-nickname">{{ order.buyer.username }}</p>
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
                                    <h1 class="font-semibold">Total Price</h1>
                                    <p class="itbms-total-order-price">{{ totalPrice(order.orderItems) }}</p>
                                </div>
                                <div>
                                    <h1 class="font-semibold">Status</h1>
                                    <p class="itbms-order-status">{{ order.orderStatus }}</p>
                                </div>
                                <div>
                                    <h1 class="font-semibold">Address</h1>
                                    <p class="itbms-shipping-address">{{ order.shippingAddress }}</p>
                                </div>
                                <div>
                                    <h1 class="font-semibold">Note</h1>
                                    <p class="itbms-order-note">{{ order.orderNote }}</p>
                                </div>
                            </div>
                            <div v-for="orderItem in order.orderItems">
                                <div class="w-full h-full bg-white rounded-lg">
                                    <div class="w-full h-full flex justify-around items-center p-3 mb-3">
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
                    </div> -->
                </div>
                <div class="w-full h-full flex justify-center items-center p-5">
                    <div class="flex justify-center items-center space-x-2">
                        <div class="space-x-2">
                            <button @click="$emit('toPageFirst')" class="border py-2 px-4 rounded-2xl cursor-pointer">First</button>
                            <button @click="$emit('toPrevPage')" class="border py-2 px-4 rounded-2xl cursor-pointer">Perv</button>
                        </div>
                        <div v-for="index in pageNumberArr" :key="index">
                            <button @click="$emit('fecthItemFromPage', index)" class="border py-2 px-4 rounded-2xl cursor-pointer">
                                {{ index }}
                            </button>
                        </div>
                        <div>
                            <button @click="$emit('toNextPage')" class="border py-2 px-4 rounded-2xl cursor-pointer">Next</button>
                        </div>
                        <div>
                            <button @click="$emit('toPageLast')" class="border py-2 px-4 rounded-2xl cursor-pointer">Last</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>