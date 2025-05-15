<script setup>

const props = defineProps({
    brands: {
        type: Array,
        required: true
    }
})
</script>

<template>
    <div>
        <div>
            <div class="overflow-x-auto rounded-box border border-base-content/5 bg-base-100">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Brand Name</th>
                            <th>Website URL</th>
                            <th>is Active</th>
                            <th>Country of Origin</th>
                            <th>Created On</th>
                            <th>Updated On</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="(brand, index) in brands" :key="index">
                            <th>{{ brand.id }}</th>
                            <td>{{ brand.name }}</td>
                            <a :href="brand.websiteUrl" target="_blank" class="underline">
                                <td>{{ brand.websiteUrl }}</td>
                            </a>
                            <th>{{ brand.isActive }}</th>
                            <td>{{ brand.countryOfOrigin }}</td>
                            <td>{{ brand.createdOn }}</td>
                            <td>{{ brand.updatedOn }}</td>
                            
                        </tr>
                        </tbody>
                    </table>
                </div>
        </div>
    </div>
</template>