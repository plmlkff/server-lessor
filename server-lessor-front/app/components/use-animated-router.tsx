"use client";
import { useRouter } from "next/navigation";

export default function useAnimatedRouter() {
    const router = useRouter();
    const viewTransitionsStatus = () => {
        const extendedDocument = document;
        let status = "Your browser doesn't support View Transitions API";

        if (extendedDocument?.startViewTransition()) {
            status = "Your browser support View Transitions API";
        }
        return status;
    };

    const animatedRoute = (url: string) => {
        const extendedDocument = document;
        if (!extendedDocument.startViewTransition) {
            return router.push(url);
        } else {
            extendedDocument.startViewTransition(() => {
                router.push(url);
            });
        }
    };
    return { animatedRoute, viewTransitionsStatus };
}