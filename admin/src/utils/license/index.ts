import security from "@/utils/license/security";
import * as process from "node:process";

const SERVER = server();
const APP_ID = appId();
const LICENSE = licenseCode();
const AES_KEY = aesKey();
const AES_IV = aesIv();

function server(): string {
  return process.env?.LICENSE_SERVER ?? "http://localhost:80";
}

function appId(): string {
  return process.env?.LICENSE_APP_ID ?? "00000000000000000000000000000000";
}

function licenseCode(): string {
  return process.env?.LICENSE_CODE ?? "00000000000000000000000000000000";
}

function aesKey(): string {
  return LICENSE.split("").reduce(
    (acc, char, idx) => (idx % 2 === 0 ? acc + char : acc),
    ""
  );
}

function aesIv(): string {
  return LICENSE.split("").reduce(
    (acc, char, idx) => (idx % 2 === 1 ? acc + char : acc),
    ""
  );
}

export function encrypt(plains: string): any {
  let cipher = "";
  if (plains) {
    cipher = security.aesEncrypt(plains, AES_KEY, AES_IV);
  }
  return cipher;
}

export function decrypt(cipher: string): any {
  let plains = "";
  if (cipher) {
    plains = security.aesDecrypt(cipher, AES_KEY, AES_IV);
  }
  return plains;
}

const license = {
  SERVER,
  APP_ID,
  LICENSE,
  encrypt,
  decrypt
};

export default license;
