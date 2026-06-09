; Teste 2: Variável fora de escopo
(define minha-funcao
    (lambda (parametro)
        (+ parametro 1)))

; Tentando usar 'parametro' fora da função lambda!
(+ parametro 10)