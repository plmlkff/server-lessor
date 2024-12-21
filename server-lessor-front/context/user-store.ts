import {create} from "zustand";
import {AppRouterInstance} from "next/dist/shared/lib/app-router-context.shared-runtime";
import {GetSubscription} from "@/api/transaction";

export interface Subscription {
	id: string
	tariffName: string
	expirationTime: number
	creationTime: number
}

interface User {
	inited: boolean;
	login: string | null;
	token: string | null;
	isAdmin: boolean;
	ref_code: number | null;
	subscription: Subscription | null;
	Login: (login: string, token: string, isAdmin: boolean, refCode: number) => void;
	Logout: () => void;
	init: (router: AppRouterInstance, pathname: string) => void;
	UpdateSubscription: () => Promise<Subscription | null>
}

export const useUserStore = create<User>((set) => ({
    inited: false,
	login: null,
	token: null,
	isAdmin: false,
	subscription: null,
	ref_code: null,
	Login: function (login: string, token: string, isAdmin: boolean, refCode: number) {
		localStorage.setItem("login", login);
		localStorage.setItem("token", token);
		localStorage.setItem("isAdmin", isAdmin ? "true" : "false");
		localStorage.setItem("ref_code", String(refCode));
		set({login: login, token: token, isAdmin: isAdmin, ref_code: refCode});
		useUserStore.getState().UpdateSubscription();
	},
	Logout: function () {
		console.log('logout')
		localStorage.removeItem("login");
		localStorage.removeItem("token");
		localStorage.removeItem("isAdmin");
		set({login: null, token: null, isAdmin: false, subscription: null});
	},
	init: async function(router, pathname) {
		const login = localStorage.getItem("login");
		const token = localStorage.getItem("token");
		const isAdmin = localStorage.getItem("isAdmin") === "true";
		const refCode = localStorage.getItem("ref_code");
		set({token: token});
		await useUserStore.getState().UpdateSubscription();
		set({login: login, isAdmin, inited: true, ref_code: +(refCode || 0) });
		if(!login && pathname !== "/login" && pathname !== "/register") router.push("/login");
	},
	UpdateSubscription: async function () {
		if(!this.token) return null;
		const sub = await GetSubscription();
		set({subscription: sub})
		return sub;
	}
}));
