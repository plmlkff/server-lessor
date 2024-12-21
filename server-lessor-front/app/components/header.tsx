'use client'
import Link from "next/link";
import "./header.css";
import {usePathname} from "next/navigation";
import {useUserStore} from "@/context/user-store";


interface NavigationRoute {
    path: string;
    isAdmin: boolean;
}

const route = (path: string, isAdmin: boolean = false): NavigationRoute => {
    return {path: path, isAdmin: isAdmin};
};

const NAVIGATION_ROUTES: { [key: string]: NavigationRoute } = {
    "Подписки": route('/'),
    "Конфиги": route('/configs'),
    "Профиль": route('/profile'),
    "Сервера": route('/servers', true),
}

export default function Header() {
    const path = usePathname();
    const isAdmin = useUserStore(state => state.isAdmin);

    return (
        <header className="header">
            {Object.keys(NAVIGATION_ROUTES).filter(e => !NAVIGATION_ROUTES[e].isAdmin || NAVIGATION_ROUTES[e].isAdmin == isAdmin).map((key, index) =>
                <Link className={"header-link" + (path == NAVIGATION_ROUTES[key].path ? " disabled" : "")} href={NAVIGATION_ROUTES[key].path}
                      key={index}>
                    {key}
                </Link>
            )}
        </header>
    )
}