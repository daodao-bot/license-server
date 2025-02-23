import { defineStore } from "pinia";
import { store, router, resetRouter, routerArrays } from "../utils";
import {
  type UserData,
  type LoginParam,
  userInfo,
  userLogin,
  userLogout
} from "@/api/user";
import { useMultiTagsStoreHook } from "./multiTags";
import {
  setToken,
  removeToken,
  getToken,
  setUser,
  removeUser
} from "@/utils/auth";

export const useUserStore = defineStore({
  id: "license-user",
  state: () => ({
    user: null as unknown as UserData,
    username: null as unknown as string,
    nickname: null as unknown as string,
    avatar: null as unknown as string,
    roles: [] as string[],
    permissions: [] as string[]
  }),
  actions: {
    setUser(userData: UserData) {
      this.user = userData;
      this.username = userData.username;
      setUser(userData);
    },

    async getUser() {
      if (this.user) {
        return this.user;
      }
      const token = getToken();
      if (token) {
        await this.info();
        return this.user;
      }
      return null as unknown as UserData;
    },

    async getRoles() {
      return this.roles;
    },

    async getPermissions() {
      return this.permissions;
    },

    /** 登入 */
    async login(param: LoginParam) {
      await userLogin({ param }).then(({ data }) => {
        const token: string = data.token;
        setToken(token);
      });
      await this.info();
    },

    async info() {
      await userInfo({ param: {} }).then(({ data }) => {
        const user: UserData = data;
        this.setUser(user);
      });
    },

    /** 前端登出（不调用接口） */
    async logout() {
      const token: string = getToken();
      if (token) {
        await userLogout({ param: {} });
      }
      removeToken();
      removeUser();
      useMultiTagsStoreHook().handleTags("equal", [...routerArrays]);
      resetRouter();
      await router.push("/login").then(_ => {});
      window.location.reload();
    }
  }
});

export function useUserStoreHook() {
  return useUserStore(store);
}
