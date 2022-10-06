import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency{
    МЕНЮ(1), ЗАКАЗАТЬ(2);
    private final int id;
}

