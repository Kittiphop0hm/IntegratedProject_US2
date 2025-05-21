<script setup>
import { onMounted, ref } from 'vue';
import { getItems } from '@/libs/fetchUtil';

const emit = defineEmits(['filterSaleItemByBrand'])
const filterBrand = ref([])
const isDropFilterBrand = ref(false)
const brands = ref([])

onMounted(async () => {
    try {
        brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`)
        brands.value.sort((a, b) => a.name.localeCompare(b.name));
    } catch (err) {
        console.log(err);
    }
})

const clearFilterBramd = () => {
    filterBrand.value = []
    isDropFilterBrand.value = false
    emit('filterSaleItemByBrand', filterBrand.value)
}

const deleteBrand = (index) => {
    filterBrand.value.splice(index, 1)
    emit('filterSaleItemByBrand', filterBrand.value)
}
</script>

<template>
    <div class="p-10">
        <div class="w-full h-[80px] flex justify-center items-center">
            <button class="flex justify-start items-center border border-gray-300 w-[80%] h-full rounded-lg overflow-x-auto cursor-pointer hover:border-gray-500">
                <div class="flex flex-row space-x-3 px-2">
                    <div v-for="(brand, index) in filterBrand" :key="index" class="itbms-brand-filter flex flex-row items-center bg-gray-100 border border-gray-300 rounded-lg px-2 py-1 space-x-2">
                        <span class="itbms-filter-item text-sm text-gray-700">{{ brand }}</span>
                        <button @click="deleteBrand(index)" class="itbms-brand-filter-clear">
                            <svg xmlns="http://www.w3.org/2000/svg" 
                                class="cursor-pointer text-red-500 hover:text-red-700" 
                                width="16" height="16" viewBox="0 0 24 24">
                                <path fill="currentColor" d="M19 7L17.59 5.59L12 11.17L6.41 5.59L5 7l5.59 5.59L5 18.17l1.41 1.42L12 13.83l5.59 5.59L19 18.17l-5.59-5.59z"></path>
                            </svg>
                        </button>
                    </div>
                </div>
            </button>
            <button @click="isDropFilterBrand = !isDropFilterBrand" class="itbms-brand-filter-button bg-gray-400 rounded-lg h-[40px] ml-3 cursor-pointer hover:opacity-80">
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="32" viewBox="0 0 24 24"><!-- Icon from Material Symbols by Google - https://github.com/google/material-design-icons/blob/master/LICENSE --><path fill="currentColor" d="m12 15l-5-5h10z"/></svg>
            </button>
            <button @click="clearFilterBramd" class="itbms-brand-filter-clear bg-gray-400 rounded-lg w-[10%] h-full ml-3 cursor-pointer hover:opacity-80">
                Clear
            </button>
        </div>

        <div 
            v-show="isDropFilterBrand" 
            class="w-full flex flex-col items-center rounded-lg p-4 shadow-md space-y-4">
                <div 
                v-for="brand in brands" 
                :key="brand.id" 
                class="flex flex-row items-center space-x-3 w-[90%] max-w-sm p-2 bg-white rounded-lg shadow-sm border border-gray-200">
                    <input 
                    @change="$emit('filterSaleItemByBrand', filterBrand)" 
                    type="checkbox" 
                    :value="brand.name" 
                    v-model="filterBrand" 
                    class="h-5 w-5 text-blue-500 border-gray-300 rounded focus:ring-2 focus:ring-blue-300">
                    <label class="text-gray-700 text-sm font-medium">{{ brand.name }}</label>
                </div>
            </div>
    </div>
</template>