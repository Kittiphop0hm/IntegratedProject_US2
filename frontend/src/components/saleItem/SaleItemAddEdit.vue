<script setup>
import Navbar from "@/views/Navbar.vue";
import SaleItemDetailModel from "../model/SaleItemDetailModel.vue";
</script>
<template>
  <Navbar />
  <SaleItemDetailModel>
    <template #path>
      <span class="font-semibold">New Sale Item</span>
    </template>
    <template #brand>
      <input
        type="text"
        class="ml-32 border rounded-md px-2 py-1 w-70"
        placeholder="Brand Name"
      />
    </template>
    <template #model>
      <input
        type="text"
        class="ml-32 border rounded-md px-2 py-1 w-70"
        placeholder="Model Name"
      />
    </template>
    <template #price>
      <span class="text-white">Price: </span>

      <input
        type="text"
        class=" ml-35 border text-white rounded-md px-2 py-1 w-70"
        placeholder="Price"
      />
    </template>
    <template #desc>
      <textarea
        class="mt-1 border rounded-md px-2 py-1 w-118"
        placeholder="Description"
      ></textarea>
    </template>
    <template #ram>
      <input
        type="text"
        class="ml-35 border rounded-md px-2 py-1 w-70"
        placeholder="RAM"
      />
    </template>
    <template #screen>
      <input
        type="text"
        class="ml-21 border rounded-md px-2 py-1 w-70"
        placeholder="Screen Size"
      />
    </template>
    <template #storage>
      <input
        type="text"
        class="ml-29 border rounded-md px-2 py-1 w-70"
        placeholder="Storage"
      />
    </template>
    <template #color>
      <input
        type="text"
        class="ml-35 border rounded-md px-2 py-1 w-70"
        placeholder="Color"
      />
    </template>
    <template #quantity>
      <input
        type="text"
        class="ml-7 border rounded-md px-2 py-1 w-70"
        placeholder="Quantity"
      />
    </template>
    <template #button1>
      <span class="text-white">Add</span>
    </template>
    <template #button2>
      <span class="text-white">Cancel</span>
    </template>
  </SaleItemDetailModel>

</template>
<style scoped></style>
