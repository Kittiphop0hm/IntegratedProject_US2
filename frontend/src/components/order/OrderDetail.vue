<script setup>
import { useRoute } from 'vue-router';
import { onMounted, ref } from 'vue';
import Navbar from '@/views/Navbar.vue';
import Search from '../Search.vue';
import { getItemsByIdWithToken } from '@/libs/fetchUtil';


const route = useRoute()
console.log(route.params.orderId);
const orderId = route.params.orderId
const order = ref({})
onMounted(async () => {
    const accessToken = sessionStorage.getItem('accessToken')
    order.value = await getItemsByIdWithToken(`${import.meta.env.VITE_APP_URL}/v2/orders`, orderId, accessToken)
    console.log(order.value);
})

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
                <div class="breadcrumbs text-sm">
                    <ul>
                        <li><router-link :to="{name: 'OrderHistory'}" class="text-lg">Your order</router-link></li>
                        <li><p class="font-semibold text-lg">Order detail</p></li>
                    </ul>
                </div>
                <div class="w-full h-full flex flex-col items-center border-2 border-gray-400 p-5 rounded-2xl">
                    <div class="w-full h-full flex justify-around items-center text-lg">
                        <div>
                            <div>
                                <p class="font-semibold">Order No: <span class="itbms-order-id font-normal">{{ order.id }}</span></p>
                            </div>
                            <div>
                                <p class="font-semibold">Order Date: <span class="itbms-order-date font-normal">{{ dateFormat(order.orderDate) }}</span></p>
                            </div>
                            <div>
                                <p class="font-semibold">total: <span class="itbms-total-order-price font-normal">{{ totalPrice(order.orderItems)?.toLocaleString() }}</span></p>
                            </div>
                            <div>
                                <p class="font-semibold">Shipped To: <span class="itbms-shipping-address font-normal">{{ order.shippingAddress }}</span></p>
                            </div>
                        </div>
                        <div>
                            <div>
                                <p class="font-semibold">Seller: <span class="itbms-nickname font-normal">{{ order.seller?.nickName }}</span></p>
                            </div>
                            <div>
                                <p class="font-semibold">Payment Date: <span class="itbms-payment-date font-normal">{{ dateFormat(order.paymentDate) }}</span></p>
                            </div>
                            <div>
                                <!-- <p class="font-semibold">Order Date: <span class="itbms-total-order-price font-normal">{{ totalPrice(order.orderItems)?.toLocaleString() }}</span></p> -->
                            </div>
                            <div>
                                <p class="font-semibold">Status: <span class="itbms-order-status font-normal">{{ order.orderStatus }}</span></p>
                            </div>
                        </div>
                    </div>

                    <div class="w-full h-full">
                        <div v-for="orderItem in order.orderItems" class="w-full h-[150px] bg-white rounded-2xl my-2">
                            <div class="w-full h-full flex flex-row justify-around items-center">
                                <div>
                                    <img src="/images/Iphone 14-pro-1.png" alt="SaleItem-image" width="100">
                                </div>
                                <div>
                                    <p class="itbms-item-description">{{ orderItem.description }}</p>
                                </div>
                                <div>
                                    <p>Qty: <span class="itbms-item-quantity">{{ orderItem.quantity }}</span></p>
                                </div>
                                <div>
                                    <p>Unit Price: <span class="itbms-item-price">{{ orderItem.price }}</span></p>
                                </div>
                                <div>
                                    <p>Total price: <span class="itbms-item-total-price">{{ orderItem.price * orderItem.quantity }}</span></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>