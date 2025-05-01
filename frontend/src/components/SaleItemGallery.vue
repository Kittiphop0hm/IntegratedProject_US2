<script setup>
import { RouterLink } from 'vue-router'
import { ref, onMounted } from 'vue'
import { getItems } from '../libs/fetchUtil'


const items = ref([])
const error = ref(null)


onMounted(async () => {
  try {
    const data = await getItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`)
    items.value = data
  } catch (err) {
    error.value = 'error'
    console.error(err)
  }
})

</script>

<template>
  <div class="p-10 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-6">
    <div
      v-for="item in items"
      :key="item.id"
      class="border rounded-xl shadow hover:shadow-md p-4 transition duration-300 ease-in-out cursor-pointer Itbms-row"
    >
      <router-link :to="{ name: 'SaleItemDetail', params: { id: item.id } }" class="block">
        <img
        src="/iPhone14ProMax.jpg"
        alt="phone image"
        class="w-full h-36 object-cover rounded-md mb-4"
      />
      
        <div class="text-sm text-gray-900 Itbms-brand">
          {{ item.brandName }}
        </div>

        <div class="text-lg font-semibold text-gray-800 Itbms-model">
          {{ item.model }}
        </div>

        <div class="text-sm mt-1 Itbms-storageGb">
          {{ item.storageGb }}<span class="Itbms-storageGb"> GB</span>
        </div>

        <div class="text-blue-600 text-lg font-bold mt-2 Itbms-price">
          Baht:{{ item.price }}
        </div>
      </router-link>
    </div>
  </div>

  <div v-if="error" class="text-red-500 p-4">
    {{ error }}
  </div>
</template>

