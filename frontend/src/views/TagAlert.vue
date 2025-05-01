<script setup>
import {onMounted, ref} from 'vue'

const showAlert = ref(false)
onMounted(() => {
    showAlert.value = false
})
</script>

<template>
    <div v-show="!showAlert" class="w-full max-h-[48px]">
        <div class="w-full h-[48px] bg-black">
            <div class="w-full h-full flex justify-between items-center text-center p-1">
                <h1></h1>
                <h1 class="text-white text-[12px] md:text-base">Don't miss out! Experience cutting-edge technology at an unbeatable price. 
                    <router-link to="/sale-items" class="underline text-white font-semibold hover:opacity-80">Shop now</router-link>
                </h1>
                <button @click="showAlert = !showAlert" class="cursor-pointer hover:opacity-80 md:pr-5">
                    <svg class="fill-current text-white" xmlns="http://www.w3.org/2000/svg" width="15" height="32" viewBox="0 0 24 24"><!-- Icon from Google Material Icons by Material Design Authors - https://github.com/material-icons/material-icons/blob/master/LICENSE --><path d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12z"/></svg>
                </button>
            </div>
        </div>
    </div>
</template>