<script setup>
const props = defineProps({
  items: {
    type: Array,
    required: true
  }
})
</script>

<template>
  <div>
    <table class="w-full border border-gray-300 text-sm">
      <thead class="bg-gray-100">
        <tr>
          <th class="border px-2 py-1">Id</th>
          <th class="border px-2 py-1">Brand</th>
          <th class="border px-2 py-1">Model</th>
          <th class="border px-2 py-1">RAM</th>
          <th class="border px-2 py-1">Storage</th>
          <th class="border px-2 py-1">Screen Size</th>
          <th class="border px-2 py-1">Color</th>
          <th class="border px-2 py-1">Price</th>
          <th class="border px-2 py-1">Quantity</th>
          <th class="border px-2 py-1">Actions</th>
        </tr>
      </thead>
      <tbody>
        <slot
          v-for="(item, index) in items"
          :yourItem="item"
          :key="index"
          name="listItem"
        />
        <tr v-if="items.length === 0">
          <td colspan="10" class="text-center text-gray-400 py-4">
            No sale items .
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

