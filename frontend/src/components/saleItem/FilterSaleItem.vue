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
}

</script>

<template>
    <div class="p-10">
        <div class="w-full h-[80px] flex justify-center items-center">
            <button @click="isDropFilterBrand = !isDropFilterBrand" class="border border-gray-300 w-[80%] h-full rounded-lg">
                <h1>{{ filterBrand.join(', ') }}</h1>
            </button>
            <button @click="clearFilterBramd" class="bg-gray-400 rounded-lg w-[10%] h-[100%] ml-3 cursor-pointer hover:opacity-80">
                Clear
            </button>
        </div>
        <div v-show="isDropFilterBrand" class="w-full flex flex-col justify-center items-center">
            <div v-for="brand in brands" :key="brand.id" class="flex flex-row">
                <input @change="$emit('filterSaleItemByBrand', filterBrand)" type="checkbox" :value="brand.name" v-model="filterBrand">
                <label>{{ brand.name }}</label>
            </div>
        </div>
    </div>
</template>