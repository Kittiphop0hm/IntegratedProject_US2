<script setup>
import { ref } from 'vue';

const isLove = ref(false)
</script>


<template>
    <div class="w-full max-h-[90vh]">
        <div class="w-full h-[70vh] flex flex-col justify-center items-center px-10">
            <div class="w-full text-center my-10 flex justify-around items-center">
                <div class="flex justify-center items-center">
                    <svg class="fill-orange-500" xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24"><!-- Icon from Google Material Icons by Material Design Authors - https://github.com/material-icons/material-icons/blob/master/LICENSE --><path d="m12 12.9l-2.13 2.09c-.56.56-.87 1.29-.87 2.07C9 18.68 10.35 20 12 20s3-1.32 3-2.94c0-.78-.31-1.52-.87-2.07z"/><path d="m16 6l-.44.55C14.38 8.02 12 7.19 12 5.3V2S4 6 4 13c0 2.92 1.56 5.47 3.89 6.86c-.56-.79-.89-1.76-.89-2.8c0-1.32.52-2.56 1.47-3.5L12 10.1l3.53 3.47c.95.93 1.47 2.17 1.47 3.5c0 1.02-.31 1.96-.85 2.75c1.89-1.15 3.29-3.06 3.71-5.3c.66-3.55-1.07-6.9-3.86-8.52"/></svg>
                    <h1 class="text-2xl font-bold">Hot Sales</h1>
                </div>
                <router-link to="/sale-items" class="flex justify-center items-center underline hover:opacity-70">
                    Shop now
                    <svg class="fill-current" xmlns="http://www.w3.org/2000/svg" width="20" height="32" viewBox="0 0 24 24"><!-- Icon from Material Symbols by Google - https://github.com/google/material-design-icons/blob/master/LICENSE --><path d="m14 18l-1.4-1.45L16.15 13H4v-2h12.15L12.6 7.45L14 6l6 6z"/></svg>
                </router-link>
            </div>
            <div class="grid grid-cols-4 gap-r-10">
                    <div class="w-[268px] h-[432px] px-5 bg-gray-200 rounded-lg shadow-2xl flex flex-col justify-center items-center">
                        <div class="w-full text-right">
                            <button @click="isLove = !isLove" class="cursor-pointer">
                                <svg v-show="!isLove" class="fill-current text-gray-400 " xmlns="http://www.w3.org/2000/svg" width="30" height="32" viewBox="0 0 24 24"><!-- Icon from Material Symbols by Google - https://github.com/google/material-design-icons/blob/master/LICENSE --><path d="m12 21l-1.45-1.3q-2.525-2.275-4.175-3.925T3.75 12.812T2.388 10.4T2 8.15Q2 5.8 3.575 4.225T7.5 2.65q1.3 0 2.475.55T12 4.75q.85-1 2.025-1.55t2.475-.55q2.35 0 3.925 1.575T22 8.15q0 1.15-.387 2.25t-1.363 2.412t-2.625 2.963T13.45 19.7zm0-2.7q2.4-2.15 3.95-3.687t2.45-2.675t1.25-2.026T20 8.15q0-1.5-1-2.5t-2.5-1q-1.175 0-2.175.662T12.95 7h-1.9q-.375-1.025-1.375-1.687T7.5 4.65q-1.5 0-2.5 1t-1 2.5q0 .875.35 1.763t1.25 2.025t2.45 2.675T12 18.3m0-6.825"/></svg>
                                <svg v-show="isLove" class="fill-pink-500" xmlns="http://www.w3.org/2000/svg" width="30" height="32" viewBox="0 0 24 24"><!-- Icon from Material Symbols by Google - https://github.com/google/material-design-icons/blob/master/LICENSE --><path d="m12 21l-1.45-1.3q-2.525-2.275-4.175-3.925T3.75 12.812T2.388 10.4T2 8.15Q2 5.8 3.575 4.225T7.5 2.65q1.3 0 2.475.55T12 4.75q.85-1 2.025-1.55t2.475-.55q2.35 0 3.925 1.575T22 8.15q0 1.15-.387 2.25t-1.363 2.412t-2.625 2.963T13.45 19.7z"/></svg>
                            </button> 
                        </div>
                        <div>
                            <img src="/images/Iphone-14-proMax.png" alt="">
                        </div>
                        <div class="font-bold text-center mt-4">
                            <h1 class="font-medium">Apple Iphone 14 Pro Max 512 GB</h1>
                            <p>42,900</p>
                        </div>
                        <router-link to="/sale-items">
                            <button class="w-[188px] h-[48px] mt-3 bg-black text-white rounded-lg cursor-pointer hover:border-2 hover:border-black hover:bg-gray-200 hover:text-black">
                                Buy now
                            </button>
                        </router-link>
                    </div>
                <div>
                    Phone
                </div>
                <div>
                    Phone
                </div>
                <div>
                    Phone
                </div>
            </div>
        </div>
    </div>


</template>