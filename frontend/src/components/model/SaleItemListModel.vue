<script setup>
const props = defineProps({
  items: {
    type: Array,
    required: true
  }
})
</script>

<template>
  <div class="p-10">
    <ul class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-6 ">
      <li v-for="(item, index) in items" :key="index" class="">
        <slot name="listItem" :yourItem="item">Enter item list</slot>


      </li>

    </ul>
    </div>
  <div v-show="items.length === 0 " class="flex justify-center mt-0">
    <h2 class="text-3xl font-bold text-black itbms-*">no sale item</h2>
  </div>
</template>
