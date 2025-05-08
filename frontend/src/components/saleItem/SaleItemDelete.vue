<script setup>
const emit = defineEmits(['deleteSaleItem', 'cancelDelete'])
</script>

<template>
    <div class="fixed inset-0 flex justify-center items-center backdrop-blur-2xl overflow-auto z-50">
        <div class="bg-white h-50 rounded-lg shadow-lg p-6 w-[40%] flex flex-col justify-around items-center max-lg:w-[50%] max-md:w-[90%]">
            <div class="w-full">
                <h1 class="font-black text-xl">Delete Confirmation</h1>
            </div>
            <div class="w-full">
                <p class="itmbs-massage font-semibold">Do you want to delete this sale item?</p>
            </div>
            <div class="w-full text-right">
                <button @click="$emit('cancelDelete')" class="itmbs-cancel-button bg-red-600 text-white mx-2 cursor-pointer w-[100px] h-[35px] rounded-lg hover:opacity-70">Cancel</button>
                <button @click="$emit('deleteSaleItem')" class="itmbs-confirm-button bg-green-600 text-white mx-2 cursor-pointer w-[100px] h-[35px] rounded-lg hover:opacity-70">Confirm</button>
            </div>
        </div>
    </div>
</template>