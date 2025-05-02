<script setup>
import { ref } from 'vue';

</script>

<template>
    <div class="w-full max-h-[632px]">
        <div class="w-full h-[632px] bg-[#211C24] text-white overflow-hidden flex flex-row justify-between items-center px-[100px] xl:px-[160px]">
            <div class="w-[50%]">
                <h1 class="text-4xl font-bold">ITB-MSHOP</h1>
                <p>The phone you’ve always wanted is just a click away. Grab it before it’s gone!</p>
                <router-link to="/sale-items">
                    <button class="border border-white py-3 px-10 mt-5 cursor-pointer hover:opacity-70">
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