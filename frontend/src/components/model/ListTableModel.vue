<script setup>
const props = defineProps({
  items: {
    type: Array,
    required: true,
  },
});
</script>

<template>
  <table class="w-full border border-gray-300 text-sm">
    <thead class="">
      <tr v-if="items.length > 0">
        <th
          v-for="(key, index) in Object.keys(items[0])"
          :key="index"
          class="border px-2 py-1"
        >
          {{ key }}
        </th>
        <th class="border px-2 py-1">Action</th>
      </tr>
    </thead>
    <tbody>
      <tr
        v-for="(item, index) in items"
        class="itbms-row border-b"
        :key="index"
      >
        <slot name="listItem" :yourItem="item"></slot>
        <slot name="action" :yourItem="item">Enter Action</slot>
        <!-- <td v-for="(value, key) in item" :key="key" class="border px-2 py-1">
            {{ value ?? "-" }}
          </td> -->
        <!-- <td><slot name="action" >Enter Action</slot></td> -->
      </tr>
      <tr v-if="items.length === 0">
        <td colspan="10" class="text-center text-gray-400 py-4">
          No sale item.
        </td>
      </tr>
    </tbody>
  </table>
</template>
