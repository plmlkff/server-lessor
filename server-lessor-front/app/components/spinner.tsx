import "./spinner.css";
import {CSSProperties} from "react";

export default function Spinner({size = 80, style}: { size?: number , style?: CSSProperties}) {
    const _style = {width: size * 8 / 10 + "px", height: size * 8 / 10 + "px", borderWidth: size / 10, margin: size / 10}

    return (
        <div className="lds-ring" style={{width: size + "px", height: size + "px", ...style}}>
            <div style={_style}></div>
            <div style={_style}></div>
            <div style={_style}></div>
            <div style={_style}></div>
        </div>
    )
}