(defun member (x y)
   (cond ((null y) (null x))
         ((equal x (first y)) t)
         (t (member x (rest y)))))
(defun union (x y)
   (cond ((null x) y)
         ((member (first x) y) (union (rest x) y))
         (t (cons (first x) (union (rest x) y)))))
(defun diff (x y)
   (cond ((null x) nil)
         ((member (first x) y) (diff (rest x) y))
         (t (cons (first x) (diff (rest x) y)))))
(defun intersection (x y)
   (cond ((null x) nil)
         ((member (first x) y) (cons (first x) (intersection (rest x) y)))
         (t (intersection (rest x) y))))
(defun subset (x y)
   (cond ((null x) t)
         ((member (first x) y) (subset (rest x) y))
         (t nil)))
(defun power (x)
   (cond ((null x) '(nil))
         (t (append (addall (first x) (power (rest x)))
                    (power (rest x))))))
(defun addall (x y)
   (cond ((null y) nil)
         (t (cons (cons x (first y)) (addall x (rest y))))))
(defun append (x y)
   (cond ((null x) y)
         (t (cons (car x) (append (cdr x) y)))))
