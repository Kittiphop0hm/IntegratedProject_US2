<script setup>
import { onMounted, ref } from 'vue';
import { getItems } from '@/libs/fetchUtil';

const isLove = ref(false)
const bestSaleItems = ref([])
onMounted( async () => {
    try {
        const items = await getItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`)
        bestSaleItems.value = items.slice(0, 4)
    } catch(error) {
        console.log(error);
    }
})
</script>


<template>
    <div class="w-full max-h-full">
        <div class="w-full h-[70vh] max-lg:h-full flex flex-col justify-center items-center px-10 py-10">
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
            <div class="grid grid-cols-4 gap-10 max-lg:grid-cols-2 max-md:grid-cols-1">
                <div v-for="(item, index) in bestSaleItems" :key="index"  class="w-[268px] h-[432px] px-5 bg-gray-200 rounded-lg shadow-2xl flex flex-col justify-center items-center">
                        <div>
                            <img src="/images/Iphone-14-proMax.png" alt="">
                        </div>
                        <div class="font-bold text-center mt-4">
                            <h1 class="font-medium">{{ item.model }}</h1>
                            <h1 class="font-medium">{{ item.storageGb }} GB</h1>
                            <p class="text-lg">฿{{ item.price.toLocaleString('th-TH') }}</p>
                        </div>
                    <router-link to="/sale-items">
                        <button class="w-[188px] h-[48px] mt-3 bg-black text-white rounded-lg cursor-pointer hover:border-2 hover:border-black hover:bg-gray-200 hover:text-black">
                            Buy now
                        </button>
                    </router-link>
                </div>
            </div>
        </div>
    </div>
</template>