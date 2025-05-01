import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LandingPage from '../views/LandingPage.vue'
import SaleItemDetail from '../components/SaleItemDetail.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: LandingPage
    },
    {
      path: '/sale-items',
      name: 'sale-items-home',
      component: HomeView
    },
    {
      path: '/sale-items/:id', 
      name: 'SaleItemDetail',
      component: SaleItemDetail
    }
  ]
})

export default router

