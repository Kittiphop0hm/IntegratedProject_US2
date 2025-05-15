<script setup>
import ViewBrandListModel from '../model/ViewBrandListModel.vue';

const props = defineProps({
    brands: {
        type: Array,
        required: true
    }
})

</script>

<template>
    <div class="px-8 py-5">
        <ViewBrandListModel :brands="brands">
            <template #listBrand="{ brands }"></template>
        </ViewBrandListModel>
    </div>
</template>