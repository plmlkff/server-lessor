import React from "react";

export interface Message {
    message: string;
    isError: boolean;
}


export interface MessageComponentProps {
	message: Message | null;
	style?: React.CSSProperties;
	className?: string;
	onlyError?: boolean
}

export default function MessageComponent({message, style, className, onlyError}: MessageComponentProps) {
    return (
        <p style={{
            textAlign: "center",
            color: message && message.isError ? "red" : "green",
            opacity: message ? (onlyError ? +message.isError : 1) : 0,
            ...style
        }}
           className={className}
        >{message ? message.message : "Waiting error..."}</p>
    )
}
