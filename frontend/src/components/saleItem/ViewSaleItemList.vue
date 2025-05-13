<script setup>
import { onMounted, ref } from 'vue';
import ViewSaleItemListModel from '../model/ViewSaleItemListModel.vue';
import { getItems } from '../../libs/fetchUtil.js';
import Navbar from '@/views/Navbar.vue';
import Search from '../Search.vue';

const items = ref([])
onMounted(async () => {
    try {
        items.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`);
        console.log(items.value);
    } catch (error) {
        console.log(error);
    } 
})
</script>

<template>
    <ViewSaleItemListModel :items="items">
        <template #heading>
            <Navbar/>  
        </template>

        <template #event>
            <Search/>
            <div class="w-full h-[140px] ">
                <div class="w-full h-full flex justify-between items-center text-center px-10">
                    <div class="w-[10%] h-[60px] border-2 border-blue-600 flex justify-center items-center rounded-xl cursor-pointer hover:opacity-70">
                        <button class="itmbs-sale-item-add cursor-pointer text-blue-600">Add Sale Item</button>
                    </div>

                    <div class="w-[10%] h-[60px] border-2 border-blue-600 flex justify-center items-center rounded-xl cursor-pointer hover:opacity-70">
                        <button class="itmbs-manage-brand cursor-pointer text-blue-600">Manage Brand</button>
                    </div>
                </div>
            </div>
        </template>

        <template #listItems="{ yourItems }">
            <div>
                <h1>List Items</h1>
                <p>{{ yourItems.name }}</p>
                <p>{{ yourItems.description }}</p>
                <p>{{ yourItems.price }}</p>
            </div>

        </template>





    </ViewSaleItemListModel>

</template>