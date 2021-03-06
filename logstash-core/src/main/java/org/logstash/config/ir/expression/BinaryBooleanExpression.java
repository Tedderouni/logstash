package org.logstash.config.ir.expression;

import org.logstash.common.SourceWithMetadata;
import org.logstash.common.Util;
import org.logstash.config.ir.SourceComponent;

public abstract class BinaryBooleanExpression extends BooleanExpression {
    @Override
    public boolean sourceComponentEquals(SourceComponent sourceComponent) {
        if (sourceComponent == null) return false;
        if (this == sourceComponent) return true;
        if (this.getClass().equals(sourceComponent.getClass())) {
            BinaryBooleanExpression other = (BinaryBooleanExpression) sourceComponent;
            return (this.getLeft().sourceComponentEquals(other.getLeft()) &&
                    this.getRight().sourceComponentEquals(other.getRight()));
        }
        return false;
    }

    private final Expression left;
    private final Expression right;

    public Expression getRight() {
        return right;
    }

    public Expression getLeft() {
        return left;
    }

    protected BinaryBooleanExpression(SourceWithMetadata meta, Expression left, Expression right) {
        super(meta);
        ensureNotNull(left, "left", meta);
        ensureNotNull(right, "right", meta);
        this.left = left;
        this.right = right;
    }

    public abstract String rubyOperator();

    @Override
    public String toRubyString() {
        return "(" + getLeft().toRubyString() + rubyOperator() + getRight().toRubyString() + ")";
    }

    public String uniqueHash() {
        return Util.digest(this.getClass().getCanonicalName() + "[" + getLeft().hashSource() + "|" + getRight().hashSource() + "]");
    }

    private static void ensureNotNull(final Expression expression, final String side,
        final SourceWithMetadata meta) {
        if (expression == null) {
            throw new IllegalArgumentException(
                String.format(
                    "Failed to parse %s-hand side of conditional %s", side, String.valueOf(meta)
                )
            );
        }
    }
}
