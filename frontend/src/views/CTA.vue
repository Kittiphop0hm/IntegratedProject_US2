<script setup>
import { ref } from 'vue';

</script>

<template>
    <div class="w-full max-h-[700px]">
        <div class="w-full h-[632px] bg-[#211C24] text-white overflow-hidden flex flex-row justify-between items-center md:px-[100px] xl:px-[160px] max-md:flex-col max-md:h-[700px] max-md:py-[120px] max-md:px-[20px]">
            <div class="md:w-[50%] max-md:flex max-md:flex-col max-md:justify-center max-md:items-center">
                <h1 class="text-4xl font-bold my-3 max-md:text-3xl">ITB-MSHOP</h1>
                <p class="max-md:text-center max-md:text-sm">The phone you’ve always wanted is just a click away. Grab it before it’s gone!</p>
                <router-link to="/sale-items">
                    <button class="border border-white w-[191px] h-[56px] px-10 mt-5 rounded-lg cursor-pointer hover:opacity-70">
                        Shop now
                    </button>
                </router-link>
            </div>
            <div>
                <img src="/images/Iphone-cta.png" alt="iphone-cta">
            </div>
        </div>
    </div>
</template>