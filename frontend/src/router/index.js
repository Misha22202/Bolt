import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '@/views/public/HomeView.vue'
import AdminLoginView from '@/views/admin/AdminLoginView.vue'
import AdminDashboardView from '@/views/admin/AdminDashboardView.vue'
import ContactsView from "@/views/public/ContactsView.vue";
import AdminServicesView from "@/views/admin/AdminServicesView.vue";
import AdminMediaView from "@/views/admin/AdminMediaView.vue";
import AdminContactsView from "@/views/admin/AdminContactsView.vue";

const routes = [
    {
        path: '/',
        name: 'home',
        label: 'Главная',
        component: HomeView
    },

    {
      path: '/contacts',
      name: 'contacts',
      label: 'Контакты',
      component: ContactsView
    },

    {
        path: '/admin/login',
        name: 'admin-login',
        component: AdminLoginView,
        meta: {
            hideSiteChrome: true
        }
    },

    {
        path: '/admin',
        name: 'admin-dashboard',
        component: AdminDashboardView,
        meta: {
            hideSiteChrome: true,
            requiresAdmin: true
        }
    },

    {
        path: '/admin/services',
        name: 'admin-services',
        component: AdminServicesView,
        meta: {
            hideSiteChrome: true,
            requiresAdmin: true
        }
    },

    {
        path: '/admin/contacts',
        name: 'admin-contacts',
        component: AdminContactsView,
        meta: {
            hideSiteChrome: true,
            requiresAdmin: true
        }
    },

    {
        path: '/admin/media',
        name: 'admin-media',
        component: AdminMediaView,
        meta: {
            hideSiteChrome: true,
            requiresAdmin: true
        }
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,

    scrollBehavior(to) {
        if (to.hash) {
            return {
                el: to.hash,
                behavior: 'smooth'
            }
        }

        return {
            top: 0
        }
    }
})

router.beforeEach(async (to) => {
    if (!to.meta.requiresAdmin) {
        return true
    }

    try {
        const response = await fetch('/api/admin/auth/me', {
            credentials: 'include'
        })

        if (response.ok) {
            return true
        }
    } catch (error) {
        console.error('Не удалось проверить сессию администратора:', error)
    }

    return {
        name: 'admin-login',
        query: {
            redirect: to.fullPath
        }
    }
})

export default router