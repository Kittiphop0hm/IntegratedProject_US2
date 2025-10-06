<script setup>
import { ref } from "vue";
import Navbar from "../../views/Navbar.vue";
import Search from "../Search.vue";
import { useCartStore } from "@/stores/carts.js";
const cartStore = useCartStore();
const arrayCartItems = ref(cartStore.cartObj);
console.log(arrayCartItems.value);
</script>
<template>
  <Navbar />
  <Search />
  <div class="container border flex justify-between mt-10">
    <div class="border-red-500 border w-[60%]">
      <h1>Shopping Cart</h1>
      <div class="itbms-select-all"><input type="checkbox" value=""  />Select All</div>
      <div
        v-for="(obj, index) in arrayCartItems"
        :key="index"
        class="itbms-row border my-5 p-5 flex justify-between flex-col"
      >
        <div class="mb-5">
          <input type="checkbox" value="" class="itbms-select-nickname"/>
          <span class="itbms-nickname">{{ obj.sellerName }}</span>
        </div>
        <div
          v-for="(item, index) in obj.items"
          :key="index"
          class="flex border items-center gap-3 itbms-item-row"
        >
          <input type="checkbox" value="" class="" />
          <img
            src="/images/iPhone14ProMax.jpg"
            alt="phone image"
            class="w-20 h-25 object-cover rounded-md mb-4"
          />
          <!-- {{ Object.keys(item) }} -->
          <div class="">
            {{ item.brandName }}
          {{ item.model }}
          ( {{ item.storageGb }} GB , {{ item.color }} )
          </div>
          <button
            @click="addQtyToParent('decrease')"
            class="py-2 px-4 bg-red-400 rounded-lg"
          >
            -
          </button>
          <button>{{ item.quantity }}</button>
          <button
            @click="addQtyToParent('increase')"
            class="py-2 px-4 bg-amber-600 rounded-lg"
          >
            +
          </button>
          Price: {{ item.price }}
        </div>
      </div>
    </div>

    <div class="border-blue-500 border w-[35%]">
      <h1>Cart Summary</h1>
      <hr />
      <h2>Shipto</h2>
      <p>
        <span class="font-bold">Address</span> [Address No, Street, Subdistrict,
        District, Province, Postal Code]
      </p>
      <textarea
        class="border border-gray-300 rounded-md p-2 w-full h-32"
        placeholder="พิมพ์ข้อความที่นี่..."
      ></textarea>
      <p class="font-bold">Note</p>
            <textarea
        class="border border-gray-300 rounded-md p-2 w-full h-32"
        placeholder="Additional instructions or requests"
      ></textarea>

      <hr />
      <div><span>Total items:</span> <span></span></div>
      <div><span>Total price:</span> <span></span></div>
      <button
        class="w-[100%] bg-green-500 hover:bg-green-600 text-white font-semibold py-2 px-4 rounded-lg shadow-md transition-all duration-200"
      >
        Place Order
      </button>
    </div>
  </div>
</template>

<style scoped>
.container {
  margin: 0 auto;
}
</style>
