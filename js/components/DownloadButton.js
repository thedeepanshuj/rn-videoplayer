import {styles} from "../styles";
import {Button, View} from "react-native";

export const DownloadButton = ({buttonText, onClick, isDisabled}) => (
    <View style={styles.welcome}>
        <Button title={buttonText} onPress={onClick} disabled={isDisabled}/>
    </View>
);