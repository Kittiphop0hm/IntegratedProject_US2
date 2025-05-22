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
        <div class="p-2 mt-5 bg-gray-50 rounded-lg shadow-md">
            <div class="flex flex-col sm:flex-row items-center justify-between space-y-4 sm:space-y-0">
                <div class="flex flex-wrap items-center gap-2 w-full h-[90px] sm:w-[75%] p-2 border border-gray-300 bg-white rounded-lg shadow-sm">
                <div
                    v-for="(brand, index) in filterBrand"
                    :key="index"
                    class="flex items-center bg-gray-100 border border-gray-300 rounded-full px-3 py-1 text-sm text-gray-700">
                    <span>{{ brand }}</span>
                    <button
                    @click="deleteBrand(index)"
                    class="ml-2 text-gray-400 hover:text-red-500 focus:outline-none">
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width="16"
                        height="16"
                        fill="currentColor"
                        viewBox="0 0 24 24">
                        <path
                        d="M19 7L17.59 5.59L12 11.17L6.41 5.59L5 7l5.59 5.59L5 18.17l1.41 1.42L12 13.83l5.59 5.59L19 18.17l-5.59-5.59z"></path>
                    </svg>
                    </button>
                </div>
                </div>

                <!-- Dropdown and Clear Buttons -->
                <div class="flex items-center gap-4">
                <button
                    @click="isDropFilterBrand = !isDropFilterBrand"
                    class="flex items-center justify-center bg-blue-500 text-white rounded-lg px-4 py-2 hover:bg-blue-600 focus:outline-none">
                    <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="20"
                    height="20"
                    viewBox="0 0 24 24"
                    class="inline">
                    <path
                        fill="currentColor"
                        d="m12 15l-5-5h10z"></path>
                    </svg>
                    <span class="ml-2">Filter</span>
                </button>
                <button
                    @click="clearFilterBrand"
                    class="bg-red-500 text-white rounded-lg px-4 py-2 hover:bg-red-600 focus:outline-none">
                    Clear
                </button>
                </div>
            </div>

            <!-- Dropdown List -->
            <div
                v-show="isDropFilterBrand"
                class="bg-white border border-gray-300 rounded-lg p-4 shadow-md">
                <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                <div
                    v-for="brand in brands"
                    :key="brand.id"
                    class="flex items-center space-x-3 p-2 bg-gray-50 rounded-lg border border-gray-200 shadow-sm">
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
        </div>
</template>