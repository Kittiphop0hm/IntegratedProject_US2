<script setup>
const emit = defineEmits('cancelNotDelete')
</script>


<template>
<div class="fixed inset-0 flex justify-center items-center backdrop-blur-2xl overflow-auto z-50">
        <div class="bg-white h-50 rounded-lg shadow-lg p-6 w-[40%] flex flex-col justify-center items-center max-lg:w-[50%] max-md:w-[90%]">
            <div class="w-full text-center mb-2">
                <p class="itbms-massage font-semibold">The requested sale item does not exist.</p>
            </div>
            <div class="w-full text-center mt-2">
                <button @click="$emit('cancelNotDelete')" class="itmbs-cancel-button bg-red-600 text-white mx-2 cursor-pointer w-[100px] h-[35px] rounded-lg hover:opacity-70">Cancel</button>
            </div>
        </div>
    </div>
</template>