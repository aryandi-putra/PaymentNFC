package com.aryandi.paymentnfc.domain.base

import kotlinx.coroutines.flow.Flow

abstract class UseCase<in P, R> {
    abstract suspend operator fun invoke(params: P): Result<R>
}

abstract class FlowUseCase<in P, R> {
    abstract operator fun invoke(params: P): Flow<Result<R>>
}

abstract class NoParamUseCase<R> {
    abstract suspend operator fun invoke(): Result<R>
}

abstract class NoParamFlowUseCase<R> {
    abstract operator fun invoke(): Flow<Result<R>>
}