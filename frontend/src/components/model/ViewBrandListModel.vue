<script setup>
const props = defineProps({
  brands: {
    type: Array,
    required: true,
  },
});
</script>

<template>
  <div>
    <div>
      <div
        class="overflow-x-auto rounded-box border border-base-content/10 bg-base-10"
      >
        <table class="table">
          <thead>
            <tr v-if="brands.length > 0">
              <th
                v-for="(key, index) in Object.keys(brands[0])"
                :key="index"
                class="border px-2 py-1"
              >
                {{ key }}
              </th>
              <th class="border px-2 py-1" >Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(brand, index) in brands" :key="index">
              <th>{{ brand.id }}</th>
              <td>{{ brand.name }}</td>
              <a :href="brand.websiteUrl" target="_blank" class="underline">
                <td>{{ brand.websiteUrl }}</td>
              </a>
              <td>{{ brand.isActive }}</td>
              <td>{{ brand.countryOfOrigin }}</td>
              <td>{{ brand.createdOn }}</td>
              <td>{{ brand.updatedOn }}</td>
              <td>
                <router-link
                  :to="{ name: 'BrandEdit', params: { id: brand.id } }"
                >
                  <button class="btn btn-info mr-2">Edit</button>
                </router-link>

                <router-link>
                  <button class="btn btn-error ml-2">Delete</button>
                </router-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
