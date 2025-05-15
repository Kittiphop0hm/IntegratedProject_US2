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
      <tr v-if="items.length > 0">
  <th
    v-for="(key, index) in Object.keys(items[0]).filter(key => key!== 'description')"
    :key="index"
    class="border px-2 py-1"
  >
    {{ key }}
  </th>
  <th class="border px-2 py-1">Action</th>
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
          No sale items.
        </td>
      </tr>
    </tbody>
  </table>
</div>

</template>

